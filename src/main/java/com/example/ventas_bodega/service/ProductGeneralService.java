package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface ProductGeneralService {

    public void createIfNotExists(ProductEntity product);
    public ProductGeneralEntity findProductByBarcode(String barcode);

    // Mantenimiento > Productos generales (SUPER_ADMIN): catálogo compartido entre empresas.
    Page<GlobalProductDto> getAllProducts(String searchKey, int page, int size);
    MessageResponse updateProduct(GlobalProductDto productDto);
    MessageResponse deleteProduct(Long id);

}
