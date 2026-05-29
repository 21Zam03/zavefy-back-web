package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;

import java.util.List;

public interface InventoryService {

    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity, String event);
    public MessageResponse createHistoryStock(ProductEntity product, UserEntity userEntity, String event);

}
