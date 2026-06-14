package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;

    @Autowired
    public CatalogServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Page<ProductDto> getProductsByCompany(Long id, String barcode, String name, String stockStatus, Boolean active, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntityPage = productRepository.findProductsFromCompanyId(id, barcode, name, stockStatus, active, categoryId, pageable);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityPage.getContent()) {
            ProductDto productDto = ProductMapper.entityToDto(productEntity);
            productDtoList.add(productDto);
        }
        return new PageImpl<>(productDtoList, pageable, productEntityPage.getTotalElements());
    }

}
