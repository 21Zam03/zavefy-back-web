package com.example.ventas_bodega.service;

import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;

public interface ProductGeneralService {

    public void createIfNotExists(String barcodeOriginalFromDto, ProductEntity product);
    public ProductGeneralEntity findProductByBarcode(String barcode);

}
