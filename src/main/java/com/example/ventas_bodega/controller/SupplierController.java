package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.SupplierDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SupplierController.API_PATH)
public class SupplierController {

    public static final String API_PATH = "/api/supplier";

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getSuppliersByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) {
        Boolean activeFilter = active == null ? null : Boolean.valueOf(active);
        return new ResponseEntity<>(
                supplierService.getSuppliersByCompany(user, searchKey, activeFilter, documentType, fromDate, toDate, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long supplierId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supplierService.getSupplierById(supplierId, user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@RequestBody SupplierDto supplierDto, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supplierService.createSupplier(supplierDto, user), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDto supplierDto, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supplierService.updateSupplier(supplierDto, user), HttpStatus.OK);
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateSupplier(@RequestParam Long supplierId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supplierService.deactivateSupplier(supplierId, user), HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<?> activateSupplier(@RequestParam Long supplierId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supplierService.activateSupplier(supplierId, user), HttpStatus.OK);
    }

}
