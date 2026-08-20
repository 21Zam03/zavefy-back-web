package com.example.ventas_bodega.service;

import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.entity.CompanyEntity;

public interface CategoryService {

    CategoryEntity getOrCreate(String name, CompanyEntity company);

}
