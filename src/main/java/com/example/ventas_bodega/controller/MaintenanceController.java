package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.dto.GlobalProductDto;
import com.example.ventas_bodega.dto.UserDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.CompanyMapper;
import com.example.ventas_bodega.mapper.UserMapper;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.MaintenanceService;
import com.example.ventas_bodega.service.ProductGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(MaintenanceController.API_PATH)
public class MaintenanceController {

    public static final String API_PATH = "/api/maintenance";

    public final MaintenanceService maintenanceService;
    public final ProductGeneralService productGeneralService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, ProductGeneralService productGeneralService) {
        this.maintenanceService = maintenanceService;
        this.productGeneralService = productGeneralService;
    }

    @GetMapping("/options")
    public ResponseEntity<?> getCategories(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(maintenanceService.getCategories(user.getCompany().getCompanyId()), HttpStatus.OK);
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
        return new ResponseEntity<>(maintenanceService.createCategory(categoryDto, user.getCompany().getCompanyId()),  HttpStatus.CREATED);
    }

    @PutMapping("/categories")
    public ResponseEntity<?> updateCategory(
            @RequestBody CategoryDto categoryDto,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(maintenanceService.updateCategory(categoryDto, user.getCompany().getCompanyId()),  HttpStatus.OK);
    }

    @DeleteMapping("/categories")
    public ResponseEntity<?> deleteCategory(
            @RequestParam Long idCategory,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(maintenanceService.deleteCategory(idCategory, user), HttpStatus.OK);
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createCompany(
            @RequestParam(value = "ruc", required = false) String ruc,
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
            @RequestParam("hasPrinter") String hasPrinter,
            @RequestParam("role") String role,
            @RequestParam(value = "planId", required = false) Long planId,
            @RequestParam(value = "subscriptionStatus", required = false) String subscriptionStatus,
            @CurrentUser UserEntity user
    ) {
        CompanyDto companyDto = CompanyMapper.buildCompanyDtoFromController(null, ruc, socialReason, comertialName, address, email, phoneNumber, imageUrl, file, hasBarcode, hasPrinter);
        UserDto userDto = UserMapper.buildCompanyDtoFromController(username, password, firstName, lastName, userEmail, userPhoneNumber);
        return new ResponseEntity<>(maintenanceService.createCompany(companyDto, userDto, user, Boolean.parseBoolean(isTest), role, planId, subscriptionStatus), HttpStatus.CREATED);
    }



    @GetMapping("/clients")
    public ResponseEntity<?> getClientsByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user) {
        return new ResponseEntity<>(maintenanceService.getClientsByCompany(user, searchKey, active == null? null : Boolean.valueOf(active) , documentType, fromDate, toDate, page, size), HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> saveClient(
            @RequestBody ClientDto clientDto,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(maintenanceService.createClient(clientDto, user),  HttpStatus.CREATED);
    }

    // ==== Mantenimiento > Empresas (SUPER_ADMIN): cruza todas las empresas ====

    @GetMapping("/companies")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) Boolean active
    ) {
        return new ResponseEntity<>(maintenanceService.getAllCompanies(searchKey, active, page, size), HttpStatus.OK);
    }

    @PatchMapping("/companies/activate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> activateCompany(@RequestParam Long companyId) {
        return new ResponseEntity<>(maintenanceService.activateCompany(companyId), HttpStatus.OK);
    }

    @PatchMapping("/companies/deactivate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deactivateCompany(@RequestParam Long companyId) {
        return new ResponseEntity<>(maintenanceService.deactivateCompany(companyId), HttpStatus.OK);
    }

    // ==== Mantenimiento > Productos generales (SUPER_ADMIN): catálogo compartido ====

    @GetMapping("/products-general")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllGeneralProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey
    ) {
        return new ResponseEntity<>(productGeneralService.getAllProducts(searchKey, page, size), HttpStatus.OK);
    }

    @PutMapping("/products-general")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateGeneralProduct(@RequestBody GlobalProductDto productDto) {
        return new ResponseEntity<>(productGeneralService.updateProduct(productDto), HttpStatus.OK);
    }

    @DeleteMapping("/products-general")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteGeneralProduct(@RequestParam Long id) {
        return new ResponseEntity<>(productGeneralService.deleteProduct(id), HttpStatus.OK);
    }

}
