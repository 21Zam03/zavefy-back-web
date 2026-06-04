package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.entity.CompanyEntity;
import org.springframework.web.multipart.MultipartFile;

public class CompanyMapper {

    public static CompanyDto buildCompanyDtoFromController(
            Long id,
            String ruc,
            String socialReason,
            String comertialName,
            String address,
            String email,
            String phoneNumber,
            String imageUrl,
            MultipartFile file,
            String hasBarcode,
            String hasPrinter
    ) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(id);
        companyDto.setRuc(ruc);
        companyDto.setSocialReason(socialReason);
        companyDto.setComertialName(comertialName);
        companyDto.setAddress(address);
        companyDto.setEmail(email);
        companyDto.setPhoneNumber(phoneNumber);
        companyDto.setImageUrl(imageUrl);
        companyDto.setFile(file);
        companyDto.setHasBarcode(Boolean.parseBoolean(hasBarcode));
        companyDto.setHasPrinter(Boolean.parseBoolean(hasPrinter));
        return companyDto;
    }

    public static CompanyEntity dtoToEntity(CompanyDto companyDto) {
        CompanyEntity company = new CompanyEntity();
        company.setCompanyId(companyDto.getCompanyId());
        company.setRuc(companyDto.getRuc());
        company.setSocialReason(companyDto.getSocialReason());
        company.setComertialName(companyDto.getComertialName());
        company.setAddress(companyDto.getAddress());
        company.setEmail(companyDto.getEmail());
        company.setPhoneNumber(companyDto.getPhoneNumber());
        company.setImageUrl(companyDto.getImageUrl());
        company.setHasStock(companyDto.isHasStock());
        company.setHasBarcode(companyDto.isHasBarcode());
        company.setHasAutomaticSaved(companyDto.isHasAutomaticSaved());
        company.setTest(companyDto.isTest());

        return company;
    }

    public static CompanyDto entityToDto(CompanyEntity companyEntity) {
        CompanyDto company = new CompanyDto();
        company.setCompanyId(companyEntity.getCompanyId());
        company.setRuc(companyEntity.getRuc());
        company.setSocialReason(companyEntity.getSocialReason());
        company.setComertialName(companyEntity.getComertialName());
        company.setAddress(companyEntity.getAddress());
        company.setEmail(companyEntity.getEmail());
        company.setPhoneNumber(companyEntity.getPhoneNumber());
        company.setImageUrl(companyEntity.getImageUrl());
        company.setHasStock(companyEntity.isHasStock());
        company.setHasBarcode(companyEntity.isHasBarcode());
        company.setHasPrinter(companyEntity.isHasPrinter());
        return company;
    }

}
