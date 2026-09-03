package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.SalePaymentLineDto;
import com.example.ventas_bodega.entity.SalePaymentLineEntity;

public class SalePaymentLineMapper {

    public static SalePaymentLineEntity dtoToEntity(SalePaymentLineDto dto) {
        SalePaymentLineEntity entity = new SalePaymentLineEntity();
        entity.setMethod(dto.getMethod());
        entity.setAmount(dto.getAmount());
        entity.setCardCommissionPercent(dto.getCardCommissionPercent());
        entity.setCardCommissionAmount(dto.getCardCommissionAmount());
        return entity;
    }

}
