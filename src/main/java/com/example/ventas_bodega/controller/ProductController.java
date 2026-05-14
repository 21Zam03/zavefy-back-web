package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ProductController.API_PATH)
public class ProductController {

    public static final String API_PATH = "/api/product";
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearProducto(
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("price") String price,
            @RequestPart("categories") String categories,
            @RequestPart("active") String active,
            @RequestPart(value = "imageUrl" , required = false) String imageUrl,
            @RequestPart("quantity") String quantity,
            @RequestPart(value = "barcode", required = false) String barcode,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CurrentUser UserEntity user
    ) throws Exception {
        logger.info("Intento de crear producto para usuario: {}", user.getEmail());

        ProductDto productDto = ProductMapper.buildProductDtoFromController(null, name, description, price, categories, active, imageUrl, quantity, barcode, file);

        MessageResponse messageResponse = productService.createProduct(productDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<?> getProduct(@PathVariable String barcode) {
        return new ResponseEntity<>(productService.searchProduct(barcode), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProductsByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String stockStatus,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String categoryId,
            @CurrentUser UserEntity user
    ) {
        Long id = null;
        if(categoryId != null) {
            id = Long.valueOf(categoryId);
        }
        return new ResponseEntity(this.productService.getProductsByCompany(user.getCompany().getRuc(), barcode, name, stockStatus, active, id, page, size), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> searchProductsByCompany(
            @RequestParam(required = false) String search,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity(this.productService.searchProductsInSaleModule(user.getCompany().getRuc(), search), HttpStatus.OK);
    }

    /*
    @GetMapping
    public ResponseEntity<?> getProductByCompany(@RequestParam String barcode,  @CurrentUser UserEntity user) {
        return new ResponseEntity<>(productService.getProductByUserLogged(barcode, user.getCompany().getRuc()), HttpStatus.OK);
    }
    * */

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateProduct(@RequestParam Long id, @CurrentUser UserEntity user) throws Exception {
        return new ResponseEntity<>(productService.deactivateProduct(id, user), HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<?> activateProduct(@RequestParam Long id, @CurrentUser UserEntity user) throws Exception {
        return new ResponseEntity<>(productService.activateProduct(id, user), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @RequestParam("id") String id,
            @RequestPart("name") String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("price") String price,
            @RequestPart("categories") String categories,
            @RequestPart("active") String active,
            @RequestPart(value = "imageUrl" , required = false) String imageUrl,
            @RequestPart("quantity") String quantity,
            @RequestPart(value = "barcode", required = false) String barcode,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CurrentUser UserEntity user
    ) throws Exception {

        ProductDto productDto = ProductMapper.buildProductDtoFromController(Long.valueOf(id), name, description, price, categories, active, imageUrl, quantity, barcode, file);

        MessageResponse messageResponse = productService.updateProduct(productDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

}
