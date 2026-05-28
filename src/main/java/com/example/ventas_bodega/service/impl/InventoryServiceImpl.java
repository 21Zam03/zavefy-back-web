package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.HistoryStockEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.repository.HistoryStockRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private HistoryStockRepository historyStockRepository;

    @Autowired
    public InventoryServiceImpl(HistoryStockRepository historyStockRepository) {
        this.historyStockRepository = historyStockRepository;
    }

    @Override
    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity) {
        return null;
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

}
