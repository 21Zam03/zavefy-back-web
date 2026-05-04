package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;

public interface ConfigurationService {

    public CompanyDto getMyBussiness(Long companyId);
    public MessageResponse updateBussiness(CompanyDto companyDto, UserEntity userEntity) throws Exception;

}
