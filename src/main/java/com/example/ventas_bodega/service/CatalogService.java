package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface CatalogService {

    public Page<ProductDto> getProductsByCompany(Long id, String barcode, String name, String stockStatus, Boolean active, Long categoryId, int page, int size);


}
