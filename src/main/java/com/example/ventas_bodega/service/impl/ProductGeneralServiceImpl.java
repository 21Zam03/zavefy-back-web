package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.GlobalProductMapper;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.ProductGeneralRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.ProductGeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<GlobalProductDto> getAllProducts(String searchKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductGeneralEntity> products = productGeneralRepository.findAllWithFilters(searchKey, pageable);
        return products.map(GlobalProductMapper::entityToDto);
    }

    @Override
    public MessageResponse updateProduct(GlobalProductDto productDto) {
        ProductGeneralEntity entity = productGeneralRepository.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException("El producto no existe"));

        String newBarcode = productDto.getBarcode();
        if (newBarcode != null && !newBarcode.equals(entity.getBarcode())) {
            ProductGeneralEntity existing = productGeneralRepository.findByBarcode(newBarcode);
            if (existing != null && !existing.getId().equals(entity.getId())) {
                return new MessageResponse("Ya existe otro producto con ese código de barras", false);
            }
            entity.setBarcode(newBarcode);
        }

        entity.setName(productDto.getName());
        entity.setDescription(productDto.getDescription());
        entity.setPrice(productDto.getPrice());
        entity.setCategory(productDto.getCategory());
        productGeneralRepository.save(entity);

        return new MessageResponse("Producto actualizado exitosamente", true);
    }

    @Override
    public MessageResponse deleteProduct(Long id) {
        if (!productGeneralRepository.existsById(id)) {
            throw new NotFoundException("El producto no existe");
        }
        productGeneralRepository.deleteById(id);
        return new MessageResponse("Producto eliminado exitosamente", true);
    }

}
