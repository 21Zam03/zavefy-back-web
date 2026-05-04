package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.MaintenanceService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MaintenanceController.API_PATH)
public class MaintenanceController {

    public static final String API_PATH = "/api/maintenance";

    public final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/options")
    public ResponseEntity<?> getCategories(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(maintenanceService.getCategories(Long.valueOf(user.getUserId())), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategoriesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(maintenanceService.getCategoriesWithPagination(Long.valueOf(user.getUserId()), name, page, size), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> saveCategory(
            @RequestBody CategoryDto categoryDto,
            @CurrentUser UserEntity user
            ) {
        return new ResponseEntity<>(maintenanceService.createCategory(categoryDto, Long.valueOf(user.getUserId())),  HttpStatus.CREATED);
    }

    @GetMapping("/yapes")
    public ResponseEntity<?> getYapes(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(maintenanceService.getYapesByCompany(user.getCompany().getCompanyId()), HttpStatus.OK);
    }

}
