package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ConfigurationService {

    CompanyDto getMyBussiness(Long companyId);
    MessageResponse updateBussiness(CompanyDto companyDto, UserEntity userEntity) throws Exception;
    MessageResponse updateBusinessFiscalInfo(String socialReason, UserEntity userEntity);
    MessageResponse updateBusinessContactInfo(String comertialName, String address, String email, String phoneNumber, UserEntity userEntity);
    MessageResponse updateBusinessOperativeInfo(String hasBarcode, String hasPrinter, UserEntity userEntity);
    MessageResponse updateBusinessBrandInfo(MultipartFile file, UserEntity userEntity) throws Exception;
    MessageResponse createBusinessYape(String aliasName, String phoneNumber, boolean isDefault, MultipartFile file, UserEntity userEntity) throws Exception;
    MessageResponse updateBusinessYape(Integer yapeId, String aliasName, String phoneNumber, boolean isDefault, MultipartFile qrFile, UserEntity user) throws Exception;
    MessageResponse deleteBusinessYape(Integer yapeId, UserEntity user);

}
