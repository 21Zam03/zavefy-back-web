package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.*;
import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.mapper.*;
import com.example.ventas_bodega.repository.*;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private YapeRepository yapeRepository;
    private CategoryClientRepository categoryClientRepository;
    private MeasurementUnitRepository measurementUnitRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public MaintenanceServiceImpl(
            CategoryRepository categoryRepository,
            YapeRepository yapeRepository,
            CategoryClientRepository categoryClientRepository,
            MeasurementUnitRepository measurementUnitRepository,
            CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.yapeRepository = yapeRepository;
        this.categoryClientRepository = categoryClientRepository;
        this.measurementUnitRepository = measurementUnitRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public MessageResponse createCategory(CategoryDto categoryDto, Long companyId) {
        CategoryEntity categoryEntity = new CategoryEntity();
        MessageResponse messageResponse = new MessageResponse();
        if(categoryDto != null) {
            categoryEntity = CategoryMapper.mapDtoToEntity(categoryDto);
            categoryEntity.setCompanyId(companyId);
            categoryEntity.setActive(true);
            CategoryEntity categoryCreated = categoryRepository.save(categoryEntity);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Categoria creada exitosamente");
        } else {
            messageResponse.setStatus(false);
            messageResponse.setMessage("Ocurrio un erro al crear la categoria");
        }
        return messageResponse;
    }

    @Override
    public List<CategoryDto> getCategories(Long userId) {
        List<CategoryDtoInter> categories = categoryClientRepository.findCategoriesByUser(userId);
        return CategoryMapper.mapInterfaceListToDtoList(categories);
    }

    @Override
    public Page<CategoryDto> getCategoriesWithPagination(Long userId, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDtoInter> categories = categoryRepository.findCategoriesByUserWithPagination(userId, name, pageable);
        List<CategoryDto> data = new ArrayList<>();
        for (int i = 0; i<categories.getContent().size(); i++) {
            data.add(CategoryMapper.mapIntefaceToDto(categories.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, categories.getTotalElements());
    }

    @Override
    public List<YapeDto> getYapesByCompany(Long companyId) {
        return YapeMapper.mapEntityListToDtoList(yapeRepository.findByCompanyId(companyId));
    }

    @Override
    public List<MeasurementUnitDto> getMeasurementUnits() {
        List<MeasurementUnitEntity> measurementUnitEntityList = measurementUnitRepository.findAll();
        return MeasurementUnitMapper.entityListToDtoList(measurementUnitEntityList);
    }

    @Override
    @Transactional
    public MessageResponse createCompany(CompanyDto companyDto, UserDto userDto, UserEntity user, boolean isTest) {
        MessageResponse messageResponse = new MessageResponse();
        try {

            CompanyEntity companyEntity = CompanyMapper.dtoToEntity(companyDto);
            CompanyEntity companyCreated = companyRepository.save(companyEntity);

            UserEntity userEntity = UserMapper.dtoToEntity(userDto);
            userEntity.setCompany(companyCreated);
            userEntity.setEnabled(true);
            userEntity.setAccountExpired(false);
            userEntity.setAccountLocked(false);
            userEntity.setCredentialExpired(false);
            userEntity.setPasswordReset(true);
            userEntity.setPasswordUpdateDate(LocalDateTime.now());
            userRepository.save(userEntity);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Empresa creada con exitosamente");
            return messageResponse;
        } catch (Exception e) {
            throw e;
        }
    }

}
