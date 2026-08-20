package com.example.ventas_bodega.validators;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public void validateForCreation(ProductDto productDto, UserEntity user) {
        CompanyEntity company = user.getCompany();
        validateCompany(company);
        validateBarcode(productDto.getBarcode(), company);
    }

    public void validateForUpdate(ProductDto productDto, ProductEntity product, UserEntity user) {
        CompanyEntity company = user.getCompany();
        validateCompany(company);
        //validateBarcodeForUpdate(productDto.getBarcode(), product.getId(), company);
    }

    private void validateBarcodeForUpdate(String barcode, Long productId, CompanyEntity company) {
        // Barcode es opcional
        if (barcode == null || barcode.isBlank()) {
            return;
        }
        boolean exists = productRepository.existsByBarcodeAndCompany_CompanyIdAndIdNot(barcode, company.getCompanyId(), productId);
        if (exists) {
            throw new BusinessException("El código de barras ya está registrado en otro producto de su inventario");
        }
    }

    private void validateCompany(CompanyEntity company) {
        if (company == null) {
            throw new BusinessException("El usuario no tiene una empresa asociada");
        }

        if (!company.isActive()) {
            throw new BusinessException("La empresa no se encuentra activa");
        }
    }

    private void validateBarcode(String barcode, CompanyEntity company) {
        // Barcode es opcional
        if (barcode == null || barcode.isBlank()) {
            return;
        }

        boolean exists = productRepository.existsByBarcodeAndCompany_CompanyId(barcode, company.getCompanyId());
        if (exists) {
            throw new BusinessException("El código de barras ya está registrado en su inventario");
        }
    }

}
