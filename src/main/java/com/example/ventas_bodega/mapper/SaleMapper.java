package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.SaleDto;
import com.example.ventas_bodega.entity.SaleEntity;
import com.example.ventas_bodega.request.SaleRequest;

public class SaleMapper {

    public static SaleEntity dtoToEntity(SaleDto saleDto) {
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setType(saleDto.getType());
        saleEntity.setSerial(saleDto.getSerial());
        saleEntity.setNumber(saleDto.getNumber());
        saleEntity.setTotal(saleDto.getTotal());
        saleEntity.setSubTotal(saleDto.getSubTotal());
        saleEntity.setSubTotalFinal(saleDto.getSubTotalFinal());
        saleEntity.setIgv(saleDto.getIgv());
        saleEntity.setMoneyType(saleDto.getMoneyType());
        saleEntity.setClientName(saleDto.getClientName());
        saleEntity.setClientDocumentNumber(saleDto.getClientDocumentNumber());
        saleEntity.setClientDocumentType(saleDto.getClientDocumentType());
        saleEntity.setClientPhoneNumber(saleDto.getClientPhoneNumber());
        saleEntity.setPaymentMethod(saleDto.getPaymentMethod());
        saleEntity.setDiscount(saleDto.getDiscount());
        return saleEntity;
    }

    public static SaleDto entityToDto(SaleEntity saleEntity) {
        SaleDto saleDto = new SaleDto();
        saleDto.setVentaId(saleEntity.getVentaId());
        saleDto.setType(saleEntity.getType());
        saleDto.setSerial(saleEntity.getSerial());
        saleDto.setNumber(saleEntity.getNumber());
        saleDto.setTotal(saleEntity.getTotal());
        saleDto.setSubTotal(saleEntity.getSubTotal());
        saleDto.setIgv(saleEntity.getIgv());
        saleDto.setCreatedDate(saleEntity.getCreatedDate());
        saleDto.setRegisterDate(saleEntity.getRegisterDate());
        saleDto.setMoneyType(saleEntity.getMoneyType());
        saleDto.setClientName(saleEntity.getClientName());
        saleDto.setPaymentMethod(saleEntity.getPaymentMethod());
        return saleDto;
    }

    public static SaleDto requestToDto(SaleRequest saleRequest) {
        SaleDto saleDto = new SaleDto();
        saleDto.setType(saleRequest.getType());
        saleDto.setSerial(saleRequest.getSerial());
        saleDto.setNumber(saleRequest.getNumber());
        saleDto.setTotal(saleRequest.getTotal());
        saleDto.setSubTotal(saleRequest.getSubTotal());
        saleDto.setIgv(saleRequest.getIgv());
        saleDto.setCreatedDate(saleRequest.getCreatedDate());
        saleDto.setSaleDetails(saleRequest.getSaleDetails());
        saleDto.setMoneyType(saleRequest.getMoneyType());
        saleDto.setClientName(saleRequest.getClientName());
        saleDto.setClientDocumentNumber(saleRequest.getClientDocumentNumber());
        saleDto.setClientDocumentType(saleRequest.getClientDocumentType());
        saleDto.setClientPhoneNumber(saleRequest.getClientPhoneNumber());
        saleDto.setPaymentMethod(saleRequest.getPaymentMethod());
        return saleDto;
    }

}
