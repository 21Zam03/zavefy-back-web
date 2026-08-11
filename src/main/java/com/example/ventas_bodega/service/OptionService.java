package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.CategoryDto;

import java.util.List;

public interface OptionService {

    public List<CategoryDto> getCategories(Long companyId);
    public boolean getStockAvailability(Long companyId);

}
