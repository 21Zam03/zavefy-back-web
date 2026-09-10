package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class GlobalProductMapper {

    public static GlobalProductDto buildDtoFromController(
            Long id,
            String barcode,
            String name,
            String description,
            BigDecimal price,
            String category,
            boolean removeImage,
            MultipartFile file
    ) {
        GlobalProductDto dto = new GlobalProductDto();
        dto.setId(id);
        dto.setBarcode(barcode);
        dto.setName(name);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setCategory(category);
        dto.setRemoveImage(removeImage);
        dto.setFile(file);
        return dto;
    }

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
