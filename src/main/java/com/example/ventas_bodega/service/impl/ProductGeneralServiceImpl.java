package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.ProductGeneralRepository;
import com.example.ventas_bodega.service.ProductGeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductGeneralServiceImpl implements ProductGeneralService {

    private final ProductGeneralRepository productGeneralRepository;

    @Override
    public void createIfNotExists(String barcodeOriginalFromDto, ProductEntity product) {
        if (barcodeOriginalFromDto == null || barcodeOriginalFromDto.isBlank()) {
            return;
        }

        ProductGeneralEntity productGeneral = ProductMapper.entityToEntityGeneral(product);
        productGeneralRepository.insertIfNotExists(
                productGeneral.getBarcode(),
                productGeneral.getName(),
                productGeneral.getDescription(),
                productGeneral.getPrice(),
                productGeneral.getCategory(),
                productGeneral.getImageUrl()
        );
    }

    @Override
    public ProductGeneralEntity findProductByBarcode(String barcode) {
        return productGeneralRepository.findByBarcode(barcode);
    }

}
