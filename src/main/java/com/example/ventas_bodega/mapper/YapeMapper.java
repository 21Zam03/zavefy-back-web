package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.YapeDto;
import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.entity.YapeEntity;

import java.util.ArrayList;
import java.util.List;

public class YapeMapper {

    public static YapeEntity dtoToEntity(YapeDto yapeDto) {
        YapeEntity yapeEntity = new YapeEntity();
        yapeEntity.setYapeId(yapeDto.getYapeId());
        yapeEntity.setPhoneNumber(yapeDto.getPhoneNumber());
        yapeEntity.setImageQr(yapeDto.getImageQr());
        yapeEntity.setDefault(yapeDto.isDefault());
        yapeEntity.setAliasName(yapeDto.getAliasName());
        return yapeEntity;
    }

    public static YapeDto entityToDto(YapeEntity yapeEntity) {
        YapeDto yapeDto = new YapeDto();
        yapeDto.setYapeId(yapeEntity.getYapeId());
        yapeDto.setPhoneNumber(yapeEntity.getPhoneNumber());
        yapeDto.setImageQr(yapeEntity.getImageQr());
        yapeDto.setDefault(yapeEntity.isDefault());
        yapeDto.setCompanyId(yapeEntity.getCompany().getCompanyId());
        yapeDto.setAliasName(yapeEntity.getAliasName());
        return yapeDto;
    }

    public static List<YapeDto> mapEntityListToDtoList(List<YapeEntity> yapeEntities) {
        List<YapeDto> yapeDtos = new ArrayList<>();
        for (YapeEntity yapeEntity : yapeEntities) {
            YapeDto yapeDto = entityToDto(yapeEntity);
            yapeDtos.add(yapeDto);
        }
        return yapeDtos;
    }

}
