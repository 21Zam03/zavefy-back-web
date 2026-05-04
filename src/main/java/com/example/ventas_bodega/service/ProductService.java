package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public MessageResponse createProduct(ProductDto productDto, UserEntity userEntity) throws Exception;
    public ProductDto searchProduct(String barcode);
    public Page<ProductDto> getProductsByCompany(String ruc, String barcode, String name, String stockStatus, Boolean active, Long categoryId, int page, int size);
    public ProductDto getProductByUserLogged(String barcode, String ruc);
    public MessageResponse updateProduct(ProductDto productDto, UserEntity userEntity) throws Exception;
    public MessageResponse deactivateProduct(Long id, UserEntity userEntity) throws Exception;
    public MessageResponse activateProduct(Long id, UserEntity userEntity) throws Exception;

    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity);

}
