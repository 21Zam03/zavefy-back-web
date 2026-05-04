package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.CompanyMapper;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.ConfigurationService;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.example.ventas_bodega.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final CompanyRepository companyRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    public ConfigurationServiceImpl(CompanyRepository companyRepository, FirebaseStorageService firebaseStorageService) {
        this.companyRepository = companyRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public CompanyDto getMyBussiness(Long companyId) {
        Optional<CompanyEntity> companyOptional = companyRepository.findById(companyId);
        if(companyOptional.isPresent()) {
            CompanyEntity companyEntity = companyOptional.get();
            return CompanyMapper.entityToDto(companyEntity);
        }
        return null;
    }

    @Override
    public MessageResponse updateBussiness(CompanyDto companyDto, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        if(companyDto.getCompanyId() == null) {
            messageResponse.setStatus(false);
            messageResponse.setMessage("El id del producto a actualizar no existe en el sistema");
            return messageResponse;
        }

        try {
            CompanyEntity companyToUpdate = companyRepository.findByRuc(userEntity.getCompany().getRuc());

            if(companyDto.getFile() != null) {
                //Cuando el cliente sube una imagen de su escritorio
                String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/"+companyToUpdate.getRuc()+"-company-logo";
                FileDto fileDto = firebaseStorageService.uploadFile(companyDto.getFile(), filePath);
                companyToUpdate.setImageUrl(fileDto.getUrl());
                companyToUpdate.setFilePath(filePath);
            }

            if(companyDto.getSocialReason() != null) {
                companyToUpdate.setSocialReason(companyDto.getSocialReason());
            }

            if(companyDto.getComertialName() != null) {
                companyToUpdate.setComertialName(companyDto.getComertialName());
            }

            if(companyDto.getAddress() != null) {
                companyToUpdate.setAddress(companyDto.getAddress());
            }

            if(companyDto.getEmail() != null) {
                companyToUpdate.setEmail(companyDto.getEmail());
            }

            if(companyDto.getPhoneNumber() != null) {
                companyToUpdate.setPhoneNumber(companyDto.getPhoneNumber());
            }

            companyToUpdate.setHasBarcode(companyDto.isHasBarcode());

            companyRepository.save(companyToUpdate);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Negocio actualizado exitosamente");
            return messageResponse;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            messageResponse.setStatus(false);
            messageResponse.setMessage("Error al actualizar el producto: "+e.getMessage());
        }
        return messageResponse;
    }

}
