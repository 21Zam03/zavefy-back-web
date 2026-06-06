package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.AdjustmentStockDto;
import com.example.ventas_bodega.dto.HistoryStockDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.dto.interfaces.HistoryStockDtoInter;
import com.example.ventas_bodega.entity.AdjustmentStockEntity;
import com.example.ventas_bodega.entity.HistoryStockEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.AdjustmentStockMapper;
import com.example.ventas_bodega.mapper.HistoryStockMapper;
import com.example.ventas_bodega.repository.AdjustmentStockRepository;
import com.example.ventas_bodega.repository.HistoryStockRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final HistoryStockRepository historyStockRepository;
    private final AdjustmentStockRepository adjustmentStockRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InventoryServiceImpl(
            HistoryStockRepository historyStockRepository,
            ProductRepository productRepository,
            AdjustmentStockRepository adjustmentStockRepository,
            JdbcTemplate jdbcTemplate) {
        this.historyStockRepository = historyStockRepository;
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.adjustmentStockRepository = adjustmentStockRepository;
    }

    @Override
    @Transactional
    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity, String event) {
        System.out.println("SIZE: "+saleDetailDtoList.size());
        MessageResponse messageResponse = new MessageResponse();
        try {
            for (SaleDetailDto saleDetailDto : saleDetailDtoList) {
                HistoryStockEntity historyStockEntity = new HistoryStockEntity();
                historyStockEntity.setEvent(event);
                historyStockEntity.setStockBefore(saleDetailDto.getStock());
                historyStockEntity.setStockAfter(saleDetailDto.getStock() - saleDetailDto.getQuantity());
                historyStockEntity.setStockVariation(historyStockEntity.getStockAfter() - historyStockEntity.getStockBefore());
                historyStockEntity.setProductId(saleDetailDto.getProductId());
                historyStockEntity.setSaleId(saleDetailDto.getSaleId());
                historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
                historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
                HistoryStockEntity historyStock = historyStockRepository.save(historyStockEntity);
                System.out.println("HISTORY CREATED: "+historyStock.toString());
            }

            // Convertimos a batch args (forma más estable en Spring Boot 4)
            List<Object[]> batchArgs = saleDetailDtoList.stream()
                    .map(item -> new Object[]{
                            item.getQuantity(),
                            item.getProductId(),
                            item.getQuantity()
                    })
                    .toList();

            int[] resultados = jdbcTemplate.batchUpdate(
                    "UPDATE tb_producto SET stock = stock - ? WHERE id_producto = ? AND stock >= ?",
                    batchArgs
            );
            System.out.println("TODO BIEN");
            // Validación de resultados
            for (int j = 0; j < resultados.length; j++) {
                if (resultados[j] == 0) {
                    throw new RuntimeException(
                            "Stock insuficiente o producto no existe. ID: " +
                                    saleDetailDtoList.get(j).getProductId()
                    );
                }
            }

            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse createHistoryStock(ProductEntity product, UserEntity userEntity, String event) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            HistoryStockEntity historyStockEntity = new HistoryStockEntity();

            historyStockEntity.setEvent(event);
            historyStockEntity.setStockBefore(0L);
            historyStockEntity.setStockAfter(Long.valueOf(product.getStock()));
            historyStockEntity.setStockVariation(historyStockEntity.getStockAfter() - historyStockEntity.getStockBefore());
            historyStockEntity.setProductId(product.getId());
            historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
            historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            historyStockRepository.save(historyStockEntity);
            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse createHistoryStock(AdjustmentStockEntity adjustmentStockEntity, UserEntity userEntity, String event) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            HistoryStockEntity historyStockEntity = new HistoryStockEntity();

            historyStockEntity.setEvent(event);
            historyStockEntity.setStockBefore(Long.valueOf(adjustmentStockEntity.getCurrentStock()));
            historyStockEntity.setStockAfter(Long.valueOf(adjustmentStockEntity.getNewStock()));
            historyStockEntity.setStockVariation(historyStockEntity.getStockAfter() - historyStockEntity.getStockBefore());
            historyStockEntity.setProductId(adjustmentStockEntity.getProductId());
            historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
            historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            historyStockRepository.save(historyStockEntity);

            int rowsAffected = jdbcTemplate.update(
                    "UPDATE tb_producto SET stock = ? WHERE id_producto = ?",
                    historyStockEntity.getStockAfter(),
                    historyStockEntity.getProductId()
            );

            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public Page<HistoryStockDto> getHistoryStockByCompany(UserEntity userEntity, String fromDate, String toDate, String event, String searchKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoryStockDtoInter> historyStock = historyStockRepository.findHistoryStockByFilters(userEntity.getCompany().getCompanyId(), searchKey, event, fromDate, toDate, pageable);
        List<HistoryStockDto> data = new ArrayList<>();
        for (int i = 0; i<historyStock.getContent().size(); i++) {
            data.add(HistoryStockMapper.interfaceToDto(historyStock.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, historyStock.getTotalElements());
    }

    @Override
    public MessageResponse createAdjustmentStock(AdjustmentStockDto adjustmentStockDto, UserEntity userEntity) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            AdjustmentStockEntity adjustmentStockEntity = AdjustmentStockMapper.dtoToEntity(adjustmentStockDto);
            adjustmentStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            AdjustmentStockEntity adjustmentStockCreated = adjustmentStockRepository.save(adjustmentStockEntity);

            createHistoryStock(adjustmentStockCreated, userEntity, "AJUSTE");
            messageResponse.setMessage("Se creo el ajuste de stock de manera exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

}
