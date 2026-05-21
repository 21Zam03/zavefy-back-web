package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.dto.SaleDto;
import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.SaleDetailEntity;
import com.example.ventas_bodega.entity.SaleEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.SaleDetailMapper;
import com.example.ventas_bodega.mapper.SaleMapper;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.SaleDetailRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.ProductService;
import com.example.ventas_bodega.service.SaleService;
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
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, SaleDetailRepository saleDetailRepository, ProductService productService, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public MessageResponse createSale(SaleDto saleDto, UserEntity userEntity) {
        MessageResponse messageResponse = new MessageResponse();

        if(saleDto == null) {
            messageResponse.setMessage("Informacion de la venta es nula");
            messageResponse.setStatus(false);
            return messageResponse;
        }

        validateDataOfSaleDto(saleDto);
        //Hacer una validacion en el backend para validar si los productos de una venta ya no tienen stock

        try {
            SaleEntity saleToCreate = SaleMapper.dtoToEntity(saleDto);
            saleToCreate.setUser(userEntity);
            SaleEntity saleCreated = saleRepository.save(saleToCreate);
            for (int i=0; i<saleDto.getSaleDetails().size(); i++) {
                //Ha largo plazo modificar este metodo en una cola para optimizar el tiempo del proceso
                if (saleDto.getSaleDetails().get(i).getProductId() == null && saleDto.getSaleDetails().get(i).isHasAutomaticSaved() && !userEntity.getCompany().isHasStock()) {
                    ProductDto productDto = new ProductDto();
                    productDto.setPrice(saleDto.getSaleDetails().get(0).getUnitePrice());
                    productDto.setName(saleDto.getSaleDetails().get(0).getName());
                    productDto.setMeasurementUnit(saleDto.getSaleDetails().get(0).getMeasurementUnit());
                    productDto.setNotes("Producto creado de forma automatica");
                    productDto.setActive(true);
                    Object[] productCreated = productService.createProduct(productDto, userEntity).getObject();

                    saleDto.getSaleDetails().get(i).setProductId(Long.parseLong(productCreated[0].toString()));
                    saleDto.getSaleDetails().get(i).setNotes(productCreated[4].toString());
                }
                SaleDetailEntity saleDetailEntity = SaleDetailMapper.dtoToEntity(saleDto.getSaleDetails().get(i));
                saleDetailEntity.setSaleEntity(saleCreated);
                saleDetailRepository.save(saleDetailEntity);
            }
            if(userEntity.getCompany().isHasStock()) {
                productService.createHistoryStock(saleDto.getSaleDetails(), userEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("ERROR: "+e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
        messageResponse.setMessage("Venta con éxito");
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
