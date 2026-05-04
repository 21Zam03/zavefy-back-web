package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.SaleDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface SaleService {

    public MessageResponse createSale(SaleDto saleDto, UserEntity userEntity);
    Page<SaleDto> getSalesByCompany(String ruc, String type, String serial, Integer number, String fromDate, String toDate, int page, int size);
    Integer getNextNumber(UserEntity user, String type, String serial);

}
