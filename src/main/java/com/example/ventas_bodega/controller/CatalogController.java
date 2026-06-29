package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.request.CartRequest;
import com.example.ventas_bodega.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CatalogController.API_PATH)
public class CatalogController {

    public static final String API_PATH = "/api/catalog";
    private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
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
            @RequestParam(required = true) Long companyId
    ) {
        Long id = null;
        if(categoryId != null) {
            id = Long.valueOf(categoryId);
        }
        return new ResponseEntity(this.catalogService.getProductsByCompany(companyId, barcode, name, stockStatus, active, id, page, size), HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<?> saveCart(@RequestBody CartRequest cartRequest) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
