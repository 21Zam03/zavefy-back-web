package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.mapper.CategoryMapper;
import com.example.ventas_bodega.repository.CategoryRepository;
import com.example.ventas_bodega.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public OptionServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getCategories(Long companyId) {
        List<CategoryEntity> categories = categoryRepository.findAllByCompanyId(companyId);
        return CategoryMapper.mapEntityListToDtoList(categories);
    }

}
