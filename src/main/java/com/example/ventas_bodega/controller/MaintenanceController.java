package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.dto.UserDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.CompanyMapper;
import com.example.ventas_bodega.mapper.UserMapper;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.MaintenanceService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/measurementUnits")
    public ResponseEntity<?> getMeasurementUnits(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(maintenanceService.getMeasurementUnits(), HttpStatus.OK);
    }

    @PostMapping(value = "/company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCompany(
            @RequestParam("ruc") String ruc,
            @RequestParam("socialReason") String socialReason,
            @RequestParam("comertialName") String comertialName,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "imageUrl" , required = false) String imageUrl,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("hasBarcode") String hasBarcode,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userPhoneNumber") String userPhoneNumber,
            @RequestParam("isTest") String isTest,
            @CurrentUser UserEntity user
    ) {
        CompanyDto companyDto = CompanyMapper.buildCompanyDtoFromController(null, ruc, socialReason, comertialName, address, email, phoneNumber, imageUrl, file, hasBarcode);
        UserDto userDto = UserMapper.buildCompanyDtoFromController(username, password, firstName, lastName, userEmail, userPhoneNumber);
        return new ResponseEntity<>(maintenanceService.createCompany(companyDto, userDto, user, Boolean.parseBoolean(isTest)), HttpStatus.CREATED);
    }

}
