package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {


    @Override
    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity) {
        return null;
    }

    @Override
    public MessageResponse createHistoryStock(ProductDto productDto, UserEntity userEntity) {

        return null;
    }


}
