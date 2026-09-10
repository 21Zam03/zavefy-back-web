package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.dto.ProductImageSetDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.GlobalProductMapper;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.ProductGeneralRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.example.ventas_bodega.service.ProductGeneralService;
import com.example.ventas_bodega.util.StoragePathUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductGeneralServiceImpl implements ProductGeneralService {

    private static final Logger log = LoggerFactory.getLogger(ProductGeneralServiceImpl.class);

    private final ProductGeneralRepository productGeneralRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final StoragePathUtil storagePathUtil;

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

        String uploadedPath = null;
        try {
            uploadedPath = processImage(productDto.getFile(), productDto.isRemoveImage(), entity);

            entity.setName(productDto.getName());
            entity.setDescription(productDto.getDescription());
            entity.setPrice(productDto.getPrice());
            entity.setCategory(productDto.getCategory());
            productGeneralRepository.save(entity);

            return new MessageResponse("Producto actualizado exitosamente", true);
        } catch (Exception e) {
            if (uploadedPath != null) {
                try {
                    firebaseStorageService.deleteProductImages(uploadedPath);
                } catch (Exception deleteException) {
                    log.error("No se pudo eliminar el archivo de Firebase: {}", uploadedPath, deleteException);
                }
            }
            throw e;
        }
    }

    // Devuelve el basePath si subió una imagen nueva (para poder compensar en Firebase si
    // el guardado en BD falla después), o null si no hubo subida.
    private String processImage(MultipartFile file, boolean removeImage, ProductGeneralEntity entity) {
        if (removeImage) {
            entity.setImageUrl(null);
            entity.setImageUrlMedium(null);
            entity.setImageUrlThumb(null);
            return null;
        }

        if (file != null && !file.isEmpty()) {
            try {
                String basePath = storagePathUtil.generalProductPath(entity.getBarcode());
                ProductImageSetDto imageSet = firebaseStorageService.uploadProductImages(file, basePath);

                entity.setImageUrl(imageSet.getLarge().getUrl());
                entity.setImageUrlMedium(imageSet.getMedium().getUrl());
                entity.setImageUrlThumb(imageSet.getThumb().getUrl());

                return basePath;
            } catch (Exception e) {
                throw new RuntimeException("No se pudo subir la imagen: " + e.getMessage(), e);
            }
        }

        return null;
    }

    @Override
    public MessageResponse deleteProduct(Long id) {
        ProductGeneralEntity entity = productGeneralRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El producto no existe"));
        productGeneralRepository.deleteById(id);
        firebaseStorageService.deleteProductImages(storagePathUtil.generalProductPath(entity.getBarcode()));
        return new MessageResponse("Producto eliminado exitosamente", true);
    }

}
