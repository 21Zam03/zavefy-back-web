package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;


public class CategoryMapper {

    public static CategoryEntity mapDtoToEntity(CategoryDto categoryDto) {
        if (categoryDto != null) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(categoryDto.getName());
            return categoryEntity;
        } return null;
    }


    public static CategoryDto mapEntityToDto(CategoryEntity categoryEntity) {
        if (categoryEntity != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(categoryEntity.getId());
            categoryDto.setName(categoryEntity.getName());
            return categoryDto;
        } return null;
    }

    public static CategoryDto mapIntefaceToDto(CategoryDtoInter categoryDtoInter) {
        if (categoryDtoInter != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(categoryDtoInter.getIdCategoria());
            categoryDto.setName(categoryDtoInter.getNombre());
            return categoryDto;
        } return null;
    }

    public static List<CategoryDto> mapInterfaceListToDtoList(List<CategoryDtoInter> categoryEntities) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (CategoryDtoInter categoryDtoInter : categoryEntities) {
            CategoryDto categoryDto = mapIntefaceToDto(categoryDtoInter);
            categoryDtos.add(categoryDto);
        }
        return  categoryDtos;
    }

    public static List<CategoryDto> mapEntityListToDtoList(List<CategoryEntity> categoryEntities) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            CategoryDto categoryDto = mapEntityToDto(categoryEntity);
            categoryDtos.add(categoryDto);
        }
        return  categoryDtos;
    }

}
