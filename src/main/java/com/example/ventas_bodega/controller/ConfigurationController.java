package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.dto.YapeDto;
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
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

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
            @RequestParam("hasPrinter") String hasPrinter,
            @RequestPart("yapes") List<YapeDto> yapes,
            @CurrentUser UserEntity user
    ) throws Exception {
        CompanyDto companyDto = CompanyMapper.buildCompanyDtoFromController(Long.valueOf(id), ruc, socialReason, comertialName, address, email, phoneNumber, imageUrl, file, hasBarcode, hasPrinter);

        MessageResponse messageResponse = configurationService.updateBussiness(companyDto, user);

        return new ResponseEntity<>(messageResponse,  HttpStatus.OK);
    }

    @PutMapping(value = "/business/fiscal")
    public ResponseEntity<?> updateBusinessFiscalInfo(
            @RequestParam("socialReason") String socialReason,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(configurationService.updateBusinessFiscalInfo(socialReason, user), HttpStatus.OK);
    }

    @PutMapping(value = "/business/contact")
    public ResponseEntity<?> updateBusinessContactInfo(
            @RequestParam("comertialName") String comertialName,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @CurrentUser UserEntity userEntity
    ) {
        return new ResponseEntity<>(configurationService.updateBusinessContactInfo(comertialName, address, email, phoneNumber, userEntity), HttpStatus.OK);
    }

    @PutMapping(value = "/business/operative")
    public ResponseEntity<?> updateBusinessOperativeInfo(
            @RequestParam("hasBarcode") String hasBarcode,
            @RequestParam("hasPrinter") String hasPrinter,
            @CurrentUser UserEntity userEntity

    ) {
        return new ResponseEntity<>(configurationService.updateBusinessOperativeInfo(hasBarcode, hasPrinter, userEntity), HttpStatus.OK);
    }

    @PutMapping(value = "/business/brand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBusinessBrandInfo(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @CurrentUser UserEntity userEntity
    ) throws Exception {
        return new ResponseEntity<>(configurationService.updateBusinessBrandInfo(file, userEntity), HttpStatus.OK);
    }

    @PostMapping(value = "/business/yape", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createYape(
            @RequestParam("aliasName") String aliasName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("isDefault") boolean isDefault,
            @RequestPart(value = "qrFile", required = false)
            MultipartFile qrFile,
            @CurrentUser UserEntity user
    ) throws Exception {

        MessageResponse response =
                configurationService.createBusinessYape(
                        aliasName,
                        phoneNumber,
                        isDefault,
                        qrFile,
                        user
                );
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            value = "/business/yape/{yapeId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateYape(
            @PathVariable Integer yapeId,
            @RequestParam("aliasName") String aliasName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("isDefault") boolean isDefault,
            @RequestPart(value = "qrFile", required = false)
            MultipartFile qrFile,
            @CurrentUser UserEntity user
    ) throws Exception {

        MessageResponse response =
                configurationService.updateBusinessYape(
                        yapeId,
                        aliasName,
                        phoneNumber,
                        isDefault,
                        qrFile,
                        user
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/business/yape/{yapeId}")
    public ResponseEntity<?> deleteYape(
            @PathVariable Integer yapeId,
            @CurrentUser UserEntity user
    ) throws Exception {

        MessageResponse response =
                configurationService.deleteBusinessYape(
                        yapeId,
                        user
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/account/personal")
    public ResponseEntity<?> updateAccountInfo(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @CurrentUser UserEntity user
    ) {
        return ResponseEntity.ok(configurationService.updateAccountInfo(firstname, lastname, email, user));
    }

}
