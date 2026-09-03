package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.dto.SaleDto;
import com.example.ventas_bodega.dto.SalePaymentLineDto;
import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.CajaEntity;
import com.example.ventas_bodega.entity.SaleDetailEntity;
import com.example.ventas_bodega.entity.SaleEntity;
import com.example.ventas_bodega.entity.SalePaymentLineEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.mapper.SaleDetailMapper;
import com.example.ventas_bodega.mapper.SaleMapper;
import com.example.ventas_bodega.mapper.SalePaymentLineMapper;
import com.example.ventas_bodega.repository.CajaRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.SaleDetailRepository;
import com.example.ventas_bodega.repository.SalePaymentLineRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final SalePaymentLineRepository salePaymentLineRepository;
    private final ProductRepository productRepository;
    private final CajaRepository cajaRepository;

    private final ProductService productService;
    private final InventoryService inventoryService;
    private final FileService fileService;
    private final AgentService agentService;
    private final ReceivableService receivableService;

    @Autowired
    public SaleServiceImpl(
            SaleRepository saleRepository,
            SaleDetailRepository saleDetailRepository,
            SalePaymentLineRepository salePaymentLineRepository,
            ProductService productService,
            ProductRepository productRepository,
            CajaRepository cajaRepository,
            InventoryService inventoryService,
            FileService fileService,
            AgentService agentService,
            ReceivableService receivableService) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.salePaymentLineRepository = salePaymentLineRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.cajaRepository = cajaRepository;
        this.inventoryService = inventoryService;
        this.fileService = fileService;
        this.agentService = agentService;
        this.receivableService = receivableService;
    }

    @Override
    @Transactional
    public MessageResponse createSale(SaleDto saleDto, UserEntity userEntity) throws Exception {
        if (saleDto == null) {
            throw new IllegalArgumentException("Información de la venta es nula");
        }
        if (saleDto.getSaleDetails() == null || saleDto.getSaleDetails().isEmpty()) {
            throw new IllegalArgumentException("La venta no tiene productos");
        }

        validateDataOfSaleDto(saleDto);

        CajaEntity cajaAbierta = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(userEntity.getCompany().getRuc())
                .orElseThrow(() -> new BusinessException("No hay una caja abierta. Abre caja antes de vender."));

        SaleEntity saleToCreate = SaleMapper.dtoToEntity(saleDto);
        saleToCreate.setUser(userEntity);
        saleToCreate.setCaja(cajaAbierta);
        saleToCreate.setIssuerRuc(userEntity.getCompany().getRuc());
        saleToCreate.setSaleLink("https://www.zavefy.com/comprobantes/" + userEntity.getCompany().getRuc()
                + "-" + saleDto.getSerial() + "-" + saleDto.getNumber());
        saleToCreate.setClientId(saleDto.getClientId());
        saleToCreate.setPartialPayment(saleDto.getPartialPayment());
        saleToCreate.setAmountPaidNow(saleDto.getAmountPaidNow());
        saleToCreate.setPendingBalance(saleDto.getPendingBalance());
        saleToCreate.setSplitPayment(saleDto.getSplitPayment());

        SaleEntity saleCreated = saleRepository.save(saleToCreate);

        for (int i = 0; i < saleDto.getSaleDetails().size(); i++) {
            SaleDetailDto detail = saleDto.getSaleDetails().get(i);

            if (detail.getProductId() == null && detail.isHasAutomaticSaved() && !userEntity.getCompany().isHasStock()) {
                ProductDto productDto = new ProductDto();
                productDto.setPrice(detail.getUnitePrice());
                productDto.setName(detail.getName());
                productDto.setMeasurementUnit(detail.getMeasurementUnit());
                productDto.setNotes("Producto creado de forma automatica");
                productDto.setActive(true);

                Object[] productCreated = productService.createProduct(productDto, userEntity).getObject();
                detail.setProductId(Long.parseLong(productCreated[0].toString()));
                detail.setNotes(productCreated[4].toString());
            }

            SaleDetailEntity saleDetailEntity = SaleDetailMapper.dtoToEntity(detail);
            saleDetailEntity.setSaleEntity(saleCreated);
            saleDetailRepository.save(saleDetailEntity);
            detail.setSaleId(Long.valueOf(saleCreated.getVentaId()));
        }

        if (Boolean.TRUE.equals(saleDto.getSplitPayment()) && saleDto.getPaymentLines() != null) {
            for (SalePaymentLineDto lineDto : saleDto.getPaymentLines()) {
                SalePaymentLineEntity lineEntity = SalePaymentLineMapper.dtoToEntity(lineDto);
                lineEntity.setSaleEntity(saleCreated);
                salePaymentLineRepository.save(lineEntity);
            }
        }

        if (Boolean.TRUE.equals(saleDto.getPartialPayment())
                && saleDto.getPendingBalance() != null
                && saleDto.getPendingBalance().compareTo(BigDecimal.ZERO) > 0) {

            if (saleDto.getClientId() == null) {
                throw new BusinessException("El pago parcial requiere un cliente registrado");
            }

            receivableService.createFromSale(saleCreated.getVentaId(), saleDto.getClientId(),
                    saleDto.getPendingBalance(), userEntity);
        }

        if (userEntity.getCompany().isHasStock()) {
            MessageResponse stockResponse = inventoryService.createHistoryStock(saleDto.getSaleDetails(), userEntity, "VENTA");
            if (!stockResponse.isStatus()) {
                // Fuerza el rollback de TODA la venta (cabecera + detalles), no solo del stock
                throw new IllegalStateException(stockResponse.getMessage());
            }
        }

        String message = "Venta con éxito";
        if(userEntity.getCompany().isHasPrinter()) {
            Long companyId = userEntity.getCompany().getCompanyId();
            Long agentId = userEntity.getCompany().getDefaultAgentId();

            if (agentService.isAgentConnected(agentId)) {
                String ticket = fileService.getTicketToPrint(Long.valueOf(saleCreated.getVentaId()));
                agentService.createPrintJob(
                        companyId,
                        agentId,
                        ticket
                );
            } else {
                message = "Venta registrada correctamente, pero no se pudo imprimir porque el agente de impresión no está conectado.";
            }
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setSaleDto(SaleMapper.entityToDto(saleCreated));
        messageResponse.setMessage(message);
        messageResponse.setStatus(true);

        return messageResponse;
    }

    @Override
    public Page<SaleDto> getSalesByCompany(String ruc, String type, String serial, Integer number, String fromDate, String toDate, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdDate"));
        Page<SaleEntity> saleEntityPage = saleRepository.findSalesByCompany(ruc, type, serial, number, fromDate, toDate, pageable);
        List<SaleDto> saleDtoList = new ArrayList<>();
        for (SaleEntity saleEntity : saleEntityPage.getContent()) {
            SaleDto saleDto = SaleMapper.entityToDto(saleEntity);
            saleDtoList.add(saleDto);
        }
        return new PageImpl<>(saleDtoList, pageable, saleEntityPage.getTotalElements());
    }

    @Override
    public Integer getNextNumber(UserEntity user, String type, String serial) {
        Integer lastNumber = null;
        try {
            lastNumber = saleRepository.findMaxNumber(type, serial, user.getCompany().getRuc());
            if(lastNumber != null) {
                lastNumber++;
            } else {
                lastNumber = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: "+ e.getMessage());
        }
        return lastNumber;
    }

    @Override
    public List<SaleDetailDto> getDetailsOfSale(UserEntity user, Long id) {
        List<SaleDetailDtoInter> detailEntityList = saleDetailRepository.findDetailsBySaleId(id);
        return SaleDetailMapper.interListToDtoList(detailEntityList);
    }

    private void validateDataOfSaleDto(SaleDto saleDto) {
        if(saleDto.getDiscount() == null) {
            saleDto.setDiscount(BigDecimal.ZERO);
        }
    }
}
