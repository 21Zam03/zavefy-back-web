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
    public void createIfNotExists(ProductEntity product) {
        // Solo los productos con código de barras internacional (no generado por el
        // sistema) deben ingresar al catálogo general compartido entre empresas.
        if (product.isBarcodeGenerated() || product.getBarcode() == null || product.getBarcode().isBlank()) {
            return;
        }

        ProductGeneralEntity productGeneral = ProductMapper.entityToEntityGeneral(product);
        productGeneralRepository.insertIfNotExists(
                productGeneral.getBarcode(),
                productGeneral.getName(),
                productGeneral.getDescription(),
                productGeneral.getPrice(),
                productGeneral.getCategory(),
                productGeneral.getImageUrl(),
                productGeneral.getImageUrlMedium(),
                productGeneral.getImageUrlThumb()
        );
    }

    @Override
    public ProductGeneralEntity findProductByBarcode(String barcode) {
        return productGeneralRepository.findByBarcode(barcode);
    }

}
