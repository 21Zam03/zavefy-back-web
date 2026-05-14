package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.SaleDetailEntity;

import java.util.ArrayList;
import java.util.List;

public class SaleDetailMapper {

    public static SaleDetailEntity dtoToEntity(SaleDetailDto saleDetailDto) {
        SaleDetailEntity saleDetailEntity = new SaleDetailEntity();
        saleDetailEntity.setQuantity(saleDetailDto.getQuantity());
        saleDetailEntity.setTotal(saleDetailDto.getTotal());
        saleDetailEntity.setUnitePrice(saleDetailDto.getUnitePrice());
        saleDetailEntity.setProductId(saleDetailDto.getProductId());
        saleDetailEntity.setName(saleDetailDto.getName());
        saleDetailEntity.setMeasurementUnit(saleDetailDto.getMeasurementUnit());
        return saleDetailEntity;
    }

    public static SaleDetailDto entityToDto(SaleDetailEntity saleDetailEntity) {
        SaleDetailDto saleDetailDto = new SaleDetailDto();
        saleDetailDto.setId(saleDetailEntity.getId());
        saleDetailDto.setQuantity(saleDetailEntity.getQuantity());
        saleDetailDto.setTotal(saleDetailEntity.getTotal());
        saleDetailDto.setUnitePrice(saleDetailEntity.getUnitePrice());
        saleDetailDto.setProductId(saleDetailEntity.getProductId());
        saleDetailDto.setMeasurementUnit(saleDetailEntity.getMeasurementUnit());
        saleDetailDto.setName(saleDetailEntity.getName());
        return saleDetailDto;
    }

    public static List<SaleDetailDto> entityListToDtoList(List<SaleDetailEntity> saleDetailEntityList) {
        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        for (SaleDetailEntity saleDetailEntity : saleDetailEntityList) {
            saleDetailDtoList.add(entityToDto(saleDetailEntity));
        }
        return  saleDetailDtoList;
    }

    public static SaleDetailDto interToDto(SaleDetailDtoInter saleDetailDtoInter) {
        SaleDetailDto saleDetailDto = new SaleDetailDto();
        saleDetailDto.setId(saleDetailDtoInter.getId());
        saleDetailDto.setQuantity(saleDetailDtoInter.getQuantity());
        saleDetailDto.setTotal(saleDetailDtoInter.getTotal());
        saleDetailDto.setUnitePrice(saleDetailDtoInter.getUnitePrice());
        saleDetailDto.setProductId(saleDetailDtoInter.getProductId());
        saleDetailDto.setNotes(saleDetailDtoInter.getNotes());
        saleDetailDto.setName(saleDetailDtoInter.getProductName());
        saleDetailDto.setMeasurementUnit(saleDetailDtoInter.getMeasurementUnit());
        return saleDetailDto;
    }

    public static List<SaleDetailDto> interListToDtoList(List<SaleDetailDtoInter> saleDetailDtoInterList) {
        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        for (SaleDetailDtoInter saleDetailDtoInter : saleDetailDtoInterList) {
            saleDetailDtoList.add(interToDto(saleDetailDtoInter));
        }
        return saleDetailDtoList;
    }
}