package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.entity.ProductGeneralEntity;

public class GlobalProductMapper {

    public static GlobalProductDto entityToDto(ProductGeneralEntity entity) {
        if (entity == null) {
            return null;
        }
        GlobalProductDto dto = new GlobalProductDto();
        dto.setId(entity.getId());
        dto.setBarcode(entity.getBarcode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setCategory(entity.getCategory());
        dto.setImageUrl(entity.getImageUrl());
        dto.setImageUrlMedium(entity.getImageUrlMedium());
        dto.setImageUrlThumb(entity.getImageUrlThumb());
        return dto;
    }

}
