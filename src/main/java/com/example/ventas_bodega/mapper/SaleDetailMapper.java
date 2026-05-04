package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.SaleDetailEntity;

public class SaleDetailMapper {

    public static SaleDetailEntity dtoToEntity(SaleDetailDto saleDetailDto) {
        SaleDetailEntity saleDetailEntity = new SaleDetailEntity();
        saleDetailEntity.setQuantity(saleDetailDto.getQuantity());
        saleDetailEntity.setTotal(saleDetailDto.getTotal());
        saleDetailEntity.setUnitePrice(saleDetailDto.getUnitePrice());
        saleDetailEntity.setProductId(saleDetailDto.getProductId());
        return saleDetailEntity;
    }

}
