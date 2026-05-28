package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.ProductFoodDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.CategoryClientRepository;
import com.example.ventas_bodega.repository.CategoryRepository;
import com.example.ventas_bodega.repository.ProductGeneralRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.rest.FoodRestTemplate;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.example.ventas_bodega.service.InventoryService;
import com.example.ventas_bodega.service.ProductService;
import com.example.ventas_bodega.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final FirebaseStorageService firebaseStorageService;
    private final FoodRestTemplate foodRestTemplate;
    private final ProductRepository productRepository;
    private final ProductGeneralRepository productGeneralRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryClientRepository categoryClientRepository;
    private final InventoryService inventoryService;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductServiceImpl(
            FirebaseStorageService firebaseStorageService,
            FoodRestTemplate foodRestTemplate,
            ProductRepository productRepository,
            ProductGeneralRepository productGeneralRepository,
            CategoryRepository categoryRepository,
            CategoryClientRepository categoryClientRepository,
            JdbcTemplate jdbcTemplate,
            InventoryService inventoryService) {
        this.firebaseStorageService = firebaseStorageService;
        this.foodRestTemplate = foodRestTemplate;
        this.productRepository = productRepository;
        this.productGeneralRepository = productGeneralRepository;
        this.categoryRepository = categoryRepository;
        this.categoryClientRepository = categoryClientRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public MessageResponse createProduct(ProductDto productDto, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();

        boolean productExist = productRepository.existsByBarcodeAndCompany_Ruc(productDto.getBarcode(), userEntity.getCompany().getRuc());
        if(productExist) {
            messageResponse.setMessage("El producto a registrar ya esta registrado en su inventario");
            messageResponse.setStatus(false);
            messageResponse.setProductDto(null);
            return messageResponse;
        }

        //LOGICA PARA CATEGORIAS
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        if(productDto.getCategories() != null) {
            CategoryEntity categoryEntity = new CategoryEntity();
            for (int i=0; i<productDto.getCategories().size(); i++) {
                categoryEntity = categoryRepository.findByName(productDto.getCategories().get(i));
                if(categoryEntity != null) {
                   categoryEntityList.add(categoryEntity);
                }
                /*
                    else {
                    categoryEntity = new CategoryEntity();
                    categoryEntity.setName(productDto.getCategories().get(i));
                    CategoryEntity categoryCreated = categoryRepository.save(categoryEntity);

                    CategoryClientEntity categoryClientEntity = new CategoryClientEntity();
                    categoryClientEntity.setCategoryId(categoryCreated.getId());
                    categoryClientEntity.setClientId(Long.valueOf(userEntity.getUserId()));
                    categoryClientRepository.save(categoryClientEntity);

                    categoryEntityList.add(categoryCreated);
                }
                */
            }
        }

        ProductEntity productToCreate = ProductMapper.dtoToEntity(productDto);
        productToCreate.setCompany(userEntity.getCompany());
        productToCreate.setCategoryEntityList(categoryEntityList);
        productToCreate.setActive(true);
        ProductEntity productCreated = productRepository.save(productToCreate);

        //LOGICA PARA IMAGEN DEL PRODUCTO
        //Si file es nulo es porque el usuario ha escogido una imagen de su escritorio
        if(productDto.getFile() == null) {
            if(productDto.getImageUrl() != null) {
                //Cuando el producto ya viene con una url para descargar la imagen
                String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/products/"+productCreated.getId();
                InputStream inputStream = downloadImage(productDto.getImageUrl());
                FileDto fileDto = firebaseStorageService.uploadFileFromUrl(inputStream, "/image/jpg", filePath);
                productCreated.setImageUrl(fileDto.getUrl());
                productCreated.setFilePath(filePath);
            }
        } else {
            //Cuando el cliente sube una imagen de su escritorio
            System.out.println("FILE SUBIDA POR EL CLIENTE MANUALMENTE: "+productDto.getFile());
            String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/products/"+"product-"+productCreated.getId();
            FileDto fileDto = firebaseStorageService.uploadFile(productDto.getFile(), filePath);
            System.out.println("ARCHIVO subido: "+fileDto.toString());
            productCreated.setImageUrl(fileDto.getUrl());
            productCreated.setFilePath(filePath);
        }

        //ACTUALIZAR EL PRODUCTO CON LA IMAGEN Y FILEPATH query optimizada
        productRepository.updateImageInfo(productCreated.getImageUrl(), productCreated.getFilePath(), productCreated.getId(), userEntity.getCompany().getCompanyId());

        //LOGICA PARA CREACION DE CODIGO DE BARRAS AUTOMATICO
        if(productCreated.getBarcode() == null) {
            String barcodeGenerated = ProductUtil.generarCodigoEAN13(productCreated.getId());
            productRepository.updateBarcodeById(productCreated.getId(), barcodeGenerated);
            productCreated.setBarcode(barcodeGenerated);
        }

        //LOGICA PARA REGISTRO DE PRODUCTO A LA TABLA PRODUCT GENERAL
        if(productCreated.getBarcode() != null) {
            boolean exists = productGeneralRepository.existsByBarcode(productCreated.getBarcode());
            if(!exists){
                ProductGeneralEntity productGeneralToCreate = ProductMapper.dtoToEntityGeneral(productDto);
                productGeneralRepository.save(productGeneralToCreate);
            }
        }

        //LOGICA DE STOCK
        if(userEntity.getCompany().isHasStock()) {
            inventoryService.createHistoryStock(productCreated, userEntity, "CREACION");
        }

        messageResponse.setMessage("El producto se ha registrado exitosamente");
        messageResponse.setStatus(true);
        messageResponse.setProductDto(ProductMapper.entityToDto(productCreated));
        messageResponse.setObject(ProductMapper.entityToObject(productCreated));

        System.out.println("RESPONSE: "+messageResponse.toString());
        return messageResponse;
    }

    @Override
    public ProductDto searchProduct(String barcode) {
        ProductGeneralEntity productGeneralEntity = productGeneralRepository.findByBarcode(barcode);
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
    @Transactional
    public MessageResponse updateProduct(ProductDto productDto, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        if(productDto.getId() == null) {
            messageResponse.setStatus(false);
            messageResponse.setMessage("El id del producto a actualizar no existe en el sistema");
            return messageResponse;
        }

        try {
            ProductEntity productToupdate = productRepository.findByBarcodeAndCompany_RucAndActive(productDto.getBarcode(), userEntity.getCompany().getRuc(), true);

            if(productDto.getFile() == null) {
                if(productDto.getImageUrl() != null) {
                    //Cuando el producto ya viene con una url para descargar la imagen
                    System.out.println("FILE: "+productDto.getImageUrl());
                    String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/products/"+productToupdate.getId();
                    InputStream inputStream = downloadImage(productDto.getImageUrl());
                    FileDto fileDto = firebaseStorageService.uploadFileFromUrl(inputStream, "/image/jpg", filePath);
                    productToupdate.setImageUrl(fileDto.getUrl());
                    productToupdate.setFilePath(filePath);
                }
            } else {
                //Cuando el cliente sube una imagen de su escritorio
                System.out.println("FILE SUBIDA POR EL CLIENTE MANUALMENTE: "+productDto.getFile());
                String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/products/"+"product-"+productToupdate.getId();
                FileDto fileDto = firebaseStorageService.uploadFile(productDto.getFile(), filePath);
                productToupdate.setImageUrl(fileDto.getUrl());
                productToupdate.setFilePath(filePath);
            }


            if(productDto.getBarcode() != null) {
                productToupdate.setBarcode(productDto.getBarcode());
            }

            if(productDto.getName() != null) {
                productToupdate.setName(productDto.getName());
            }

            if(productDto.getDescription() != null) {
                productToupdate.setDescription(productDto.getDescription());
            }

            if(productDto.getPrice() != null) {
                productToupdate.setPrice(productDto.getPrice());
            }

            if(productDto.getMeasurementUnit() != null) {
                productToupdate.setMeasurementUnit(productDto.getMeasurementUnit());
            }

            if(productToupdate.getCategoryEntityList() != null) {
                List<CategoryEntity> list = new ArrayList<>();
                if(productDto.getCategories() != null) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    for (int i=0; i<productDto.getCategories().size(); i++) {
                        //categoryClientRepository.findCategoriesByUser()
                        categoryEntity = categoryRepository.findByName(productDto.getCategories().get(i));
                        if(categoryEntity != null) {
                            list.add(categoryEntity);
                        }
                        /*
                        else {
                            categoryEntity = new CategoryEntity();
                            categoryEntity.setName(productDto.getCategories().get(i));
                            CategoryEntity categoryCreated = categoryRepository.save(categoryEntity);
                            list.add(categoryCreated);
                        }
                        * */
                    }
                }
                productToupdate.setCategoryEntityList(list);
            }

            productToupdate.setActive(true);
            productRepository.save(productToupdate);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Producto Actualizado exitosamente");
            return messageResponse;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            messageResponse.setStatus(false);
            messageResponse.setMessage("Error al actualizar el producto: "+e.getMessage());
        }

        return messageResponse;
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

    public InputStream downloadImage(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        return url.openStream();
    }

}
