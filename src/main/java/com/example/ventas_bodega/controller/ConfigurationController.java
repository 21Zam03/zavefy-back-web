package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.CompanyMapper;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ConfigurationController.API_PATH)
public class ConfigurationController {

    public static final String API_PATH = "/api/configuration";

    public final ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/bussiness")
    public ResponseEntity<?> getMyBussiness(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(configurationService.getMyBussiness(user.getCompany().getCompanyId()), HttpStatus.OK);
    }

    @PutMapping(value = "/bussiness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBussiness(
            @RequestParam("id") String id,
            @RequestParam("ruc") String ruc,
            @RequestParam("socialReason") String socialReason,
            @RequestParam("comertialName") String comertialName,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "imageUrl" , required = false) String imageUrl,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("hasBarcode") String hasBarcode,
            @CurrentUser UserEntity user
    ) throws Exception {
        CompanyDto companyDto = CompanyMapper.buildCompanyDtoFromController(Long.valueOf(id), ruc, socialReason, comertialName, address, email, phoneNumber, imageUrl, file, hasBarcode);

        MessageResponse messageResponse = configurationService.updateBussiness(companyDto, user);

        return new ResponseEntity<>(messageResponse,  HttpStatus.OK);
    }

}
