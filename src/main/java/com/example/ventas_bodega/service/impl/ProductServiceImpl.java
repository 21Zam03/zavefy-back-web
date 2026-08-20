package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.*;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.enums.StockMovementTypeEnum;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.rest.FoodRestTemplate;
import com.example.ventas_bodega.service.*;
import com.example.ventas_bodega.util.ProductUtil;
import com.example.ventas_bodega.validators.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final FirebaseStorageService firebaseStorageService;
    private final FoodRestTemplate foodRestTemplate;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final CategoryService categoryService;
    private final ProductGeneralService productGeneralService;

    private final ProductValidator productValidator;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductServiceImpl(
            FirebaseStorageService firebaseStorageService,
            CategoryService categoryService,
            FoodRestTemplate foodRestTemplate,
            ProductRepository productRepository,
            JdbcTemplate jdbcTemplate,
            InventoryService inventoryService,
            ProductValidator productValidator,
            ProductGeneralService productGeneralService) {
        this.firebaseStorageService = firebaseStorageService;
        this.foodRestTemplate = foodRestTemplate;
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.inventoryService = inventoryService;
        this.productValidator = productValidator;
        this.categoryService = categoryService;
        this.productGeneralService = productGeneralService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageResponse createProduct(ProductDto productDto, UserEntity userEntity) throws Exception {
        String uploadedFilePath = null;
        try {

            // 1. FASE DE VALIDACIONES DE NEGOCIO
            productValidator.validateForCreation(productDto, userEntity);

            // 2. LOGICA DE NEGOCIO EN CATEGORIAS
            CategoryEntity categoryCreated = categoryService.getOrCreate(productDto.getCategory(), userEntity.getCompany());

            // 3. CREACION DEL PRODUCTO A BD
            ProductEntity productToCreate = ProductMapper.dtoToEntity(productDto);
            productToCreate.setCategoryEntity(categoryCreated);
            productToCreate.setCompany(userEntity.getCompany());
            productToCreate.setActive(true);
            productToCreate.setStock(productDto.getStock() == null ? 0 : productDto.getStock());
            ProductEntity productCreated = productRepository.save(productToCreate);

            // 4. PROCESAR IMAGEN
            uploadedFilePath = processProductImage(productDto, productCreated, userEntity.getCompany());

            // 5. GENERAR CÓDIGO DE BARRAS
            if (productCreated.getBarcode() == null || productCreated.getBarcode().isBlank()) {
                String barcodeGenerated = ProductUtil.generarCodigoInterno(productCreated.getId());
                productCreated.setBarcode(barcodeGenerated);
                productRepository.save(productCreated);
            }

            // 6. SINCRONIZAR PRODUCTO CON CATÁLOGO GENERAL
            productGeneralService.createIfNotExists(productCreated);

            // 7. REGISTRAR HISTORIAL DE STOCK
            if (userEntity.getCompany().isHasStock()) {
                inventoryService.createHistoryStock(productCreated, userEntity, StockMovementTypeEnum.CREACION.name());
            }

            // 8. CONSTRUIR RESPUESTA
            return buildProductCreatedResponse(productCreated);

        } catch (Exception e) {
            //e.printStackTrace();
            // 9. COMPENSAR FIREBASE
            if (uploadedFilePath != null) {
                try {
                    firebaseStorageService.deleteFile(uploadedFilePath);
                } catch (Exception deleteException) {
                    log.error("No se pudo eliminar el archivo de Firebase: {}", uploadedFilePath, deleteException);
                    deleteException.printStackTrace();
                }
            }
            // Permite que @Transactional haga rollback
            throw e;
        }
    }

    private MessageResponse buildProductUpdatedResponse(ProductEntity product) {
        MessageResponse response = new MessageResponse();
        response.setMessage("El producto se ha actualizado correctamente");
        response.setStatus(true);
        response.setProductDto(ProductMapper.entityToDto(product));
        response.setObject(ProductMapper.entityToObject(product));
        return response;
    }

    private MessageResponse buildProductCreatedResponse(ProductEntity product) {
        MessageResponse response = new MessageResponse();
        response.setMessage("El producto se ha registrado exitosamente");
        response.setStatus(true);
        response.setProductDto(ProductMapper.entityToDto(product));
        response.setObject(ProductMapper.entityToObject(product));
        return response;
    }

    private String buildProductImagePath(String ruc, Long productId) {
        return "bodega-sistemas/clients/"
                + ruc
                + "/products/"
                + productId;
    }

    private String processProductImage(ProductDto productDto, ProductEntity productCreated, CompanyEntity company) throws Exception {
        if ((productDto.getFile() == null || productDto.getFile().isEmpty())
                && (productDto.getImageUrl() == null || productDto.getImageUrl().isBlank())) {
            return null;
        }

        String filePath = buildProductImagePath(company.getRuc(), productCreated.getId());
        FileDto fileDto;

        if (productDto.getFile() == null) {
            try (InputStream inputStream = downloadImage(productDto.getImageUrl())) {
                fileDto = firebaseStorageService.uploadFileFromUrl(
                        inputStream,
                        "image/jpeg",
                        filePath
                );
            }
        } else {
            fileDto = firebaseStorageService.uploadFile(productDto.getFile(), filePath);
        }

        productCreated.setImageUrl(fileDto.getUrl());
        productCreated.setFilePath(fileDto.getFileName());
        return fileDto.getFileName();
    }

    @Override
    public ProductDto searchProduct(String barcode) {
        ProductGeneralEntity productGeneralEntity = productGeneralService.findProductByBarcode(barcode);
        if(productGeneralEntity == null){
            ProductFoodDto p = foodRestTemplate.getProductByBarcode(barcode);
            if(p != null) {
                System.out.println("P: "+p);
                return ProductMapper.mapToInternal(p);
            } else {
                System.out.println("P: "+p);
                return new ProductDto();
            }
        } else {
            return ProductMapper.entityGeneralToDto(productGeneralEntity);
        }
    }

    @Override
    public Page<ProductDto> getProductsByCompany(String ruc, String barcode, String name, String stockStatus, Boolean active, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntityPage = productRepository.findProductsByCompanyAndBarcode(ruc, barcode, name, stockStatus, active, categoryId, pageable);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityPage.getContent()) {
            ProductDto productDto = ProductMapper.entityToDto(productEntity);
            productDtoList.add(productDto);
        }
        return new PageImpl<>(productDtoList, pageable, productEntityPage.getTotalElements());
    }

    @Override
    public ProductDto searchProductsInSaleModule(String ruc, String search) {
        ProductEntity product = productRepository.searchProductsByCompanyForSaleModule(ruc, search);
        return ProductMapper.entityToDto(product);
    }

    @Override
    public ProductDto getProductByUserLogged(String barcode, String ruc) {
        ProductEntity product = productRepository.findByBarcodeAndCompany_RucAndActive(barcode, ruc, true);
        if(product != null) {
            return ProductMapper.entityToDto(product);
        } else return new ProductDto();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageResponse updateProduct(ProductDto productDto, UserEntity userEntity) throws Exception {
        String uploadedFilePath = null;
        String oldFilePath = null;
        ProductEntity productToUpdate = productRepository.findByIdAndCompany_CompanyId(productDto.getProductId(), userEntity.getCompany().getCompanyId())
                .orElseThrow(() ->
                                new BusinessException(
                                        "El producto no existe en su inventario"
                                )
                        );
        productValidator.validateForUpdate(productDto, productToUpdate, userEntity);

        try {
            oldFilePath = productToUpdate.getFilePath();
            uploadedFilePath = processProductImageForUpdate(productDto.getFile(), productDto.isRemoveImage(), productToUpdate, userEntity.getCompany());

            CategoryEntity category = categoryService.getOrCreate(productDto.getCategory(), userEntity.getCompany());

            productToUpdate.setName(productDto.getName());
            productToUpdate.setDescription(productDto.getDescription());
            productToUpdate.setPrice(productDto.getPrice());
            productToUpdate.setMeasurementUnit(productDto.getMeasurementUnit());
            productToUpdate.setCategoryEntity(category);
            ProductEntity productUpdated = productRepository.save(productToUpdate);

            return buildProductUpdatedResponse(productUpdated);
        } catch (Exception e) {
            // 9. COMPENSAR FIREBASE
            if (uploadedFilePath != null) {
                try {
                    firebaseStorageService.deleteFile(uploadedFilePath);
                } catch (Exception deleteException) {
                    log.error("No se pudo eliminar el archivo de Firebase: {}", uploadedFilePath, deleteException);
                    deleteException.printStackTrace();
                }
            }
            // Permite que @Transactional haga rollback
            throw e;
        }
    }

    @Override
    public MessageResponse deactivateProduct(Long id, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        try {
            productRepository.deactivateProduct(id, userEntity.getCompany().getCompanyId());
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
            messageResponse.setStatus(false);
            messageResponse.setProductDto(null);
            messageResponse.setMessage(e.getMessage());
        }
        messageResponse.setStatus(true);
        messageResponse.setProductDto(null);
        messageResponse.setMessage("El producto se ha desactivado exitosamente");
        return messageResponse;
    }

    @Override
    public MessageResponse activateProduct(Long id, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        try {
            productRepository.activateProduct(id, userEntity.getCompany().getCompanyId());
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
            messageResponse.setStatus(false);
            messageResponse.setProductDto(null);
            messageResponse.setMessage(e.getMessage());
        }
        messageResponse.setStatus(true);
        messageResponse.setProductDto(null);
        messageResponse.setMessage("El producto se ha activado exitosamente");
        return messageResponse;
    }

    @Override
    @Transactional
    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity) {

        // Convertimos a batch args (forma más estable en Spring Boot 4)
        List<Object[]> batchArgs = saleDetailDtoList.stream()
                .map(item -> new Object[]{
                        item.getQuantity(),
                        item.getProductId(),
                        item.getQuantity()
                })
                .toList();

        int[] resultados = jdbcTemplate.batchUpdate(
                "UPDATE tb_producto SET stock = stock - ? WHERE id_producto = ? AND stock >= ?",
                batchArgs
        );

        // Validación de resultados
        for (int i = 0; i < resultados.length; i++) {
            if (resultados[i] == 0) {
                throw new RuntimeException(
                        "Stock insuficiente o producto no existe. ID: " +
                                saleDetailDtoList.get(i).getProductId()
                );
            }
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(true);
        return messageResponse;
    }

    private String processProductImageForUpdate(MultipartFile file, boolean removeImage, ProductEntity product, CompanyEntity company) throws Exception {

        // 1. El usuario quiere eliminar la imagen
        if (removeImage) {
            product.setImageUrl(null);
            product.setFilePath(null);
            return null;
        }

        // 2. El usuario subió una imagen
        if (file != null && !file.isEmpty()) {
            String filePath = "bodega-sistemas/clients/" + company.getRuc() + "/products/product-" + product.getId();
            FileDto fileDto = firebaseStorageService.uploadFile(file, filePath);

            product.setImageUrl(fileDto.getUrl());
            product.setFilePath(filePath);

            return filePath;
        }

        // 3. No se solicitó ningún cambio de imagen
        return null;
    }

    public InputStream downloadImage(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        return url.openStream();
    }

}
