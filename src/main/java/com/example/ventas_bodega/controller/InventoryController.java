package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(InventoryController.API_PATH)
public class InventoryController {

    public final static String API_PATH = "/api/inventory";
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/history-stock")
    public ResponseEntity<?> getHistoryStockByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(inventoryService.getHistoryStockByCompany(user, fromDate, toDate, event, searchKey, page, size),  HttpStatus.CREATED);
    }

    @PostMapping("/adjust-stock")
    public ResponseEntity<?> adjustStock(@RequestParam Long productId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
