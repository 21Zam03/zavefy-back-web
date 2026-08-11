package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.mapper.CategoryMapper;
import com.example.ventas_bodega.repository.CategoryRepository;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionServiceImpl implements OptionService {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public OptionServiceImpl(CategoryRepository categoryRepository, CompanyRepository companyRepository) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CategoryDto> getCategories(Long companyId) {
        List<CategoryEntity> categories = categoryRepository.findAllByCompanyId(companyId);
        return CategoryMapper.mapEntityListToDtoList(categories);
    }

    @Override
    public boolean getStockAvailability(Long companyId) {
        Optional<CompanyEntity> company = companyRepository.findById(companyId);
        return company.map(CompanyEntity::isHasStock).orElse(false);
    }

}
