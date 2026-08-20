package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.repository.CategoryRepository;
import com.example.ventas_bodega.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity getOrCreate(String name, CompanyEntity company) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String normalizedName = normalize(name);

        return categoryRepository.findByNameIgnoreCaseAndCompanyId(
                        normalizedName,
                        company.getCompanyId()
                )
                .orElseGet(() ->
                        createCategory(
                                name,
                                company
                        )
                );
    }

    private CategoryEntity createCategory(String name, CompanyEntity company) {
        CategoryEntity category = new CategoryEntity();

        category.setName(name.trim());
        category.setCompanyId(company.getCompanyId());
        category.setActive(true);

        return categoryRepository.save(category);
    }

    private String normalize(String name) {
        return name
                .trim()
                .replaceAll("\\s+", " ")
                .toLowerCase();
    }

}
