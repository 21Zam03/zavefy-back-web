package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PurchaseController.API_PATH)
public class PurchaseController {

    public static final String API_PATH = "/api/purchase";

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getPurchasesByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(
                purchaseService.getPurchasesByCompany(user, searchKey, supplierId, fromDate, toDate, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Long purchaseId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(purchaseService.getPurchaseById(purchaseId, user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseDto purchaseDto, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(purchaseService.createPurchase(purchaseDto, user), HttpStatus.CREATED);
    }

}
