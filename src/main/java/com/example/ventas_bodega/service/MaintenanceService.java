package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.MeasurementUnitDto;
import com.example.ventas_bodega.dto.YapeDto;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaintenanceService {

    public MessageResponse createCategory(CategoryDto categoryDto, Long userId);
    public List<CategoryDto> getCategories(Long UserId);
    public Page<CategoryDto> getCategoriesWithPagination(Long UserId, String name, int page, int size);
    public List<YapeDto> getYapesByCompany(Long companyId);
    public List<MeasurementUnitDto> getMeasurementUnits();

}
