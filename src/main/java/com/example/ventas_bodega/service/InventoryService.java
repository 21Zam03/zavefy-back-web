package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;

import java.util.List;

public interface InventoryService {

    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity);
    public MessageResponse createHistoryStock(ProductDto productDto, UserEntity userEntity);

}
