package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CategoryDto;
import com.example.ventas_bodega.dto.YapeDto;
import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.entity.CategoryClientEntity;
import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.mapper.CategoryMapper;
import com.example.ventas_bodega.mapper.YapeMapper;
import com.example.ventas_bodega.repository.CategoryClientRepository;
import com.example.ventas_bodega.repository.CategoryRepository;
import com.example.ventas_bodega.repository.YapeRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private CategoryRepository categoryRepository;
    private YapeRepository yapeRepository;
    private CategoryClientRepository categoryClientRepository;

    @Autowired
    public MaintenanceServiceImpl(CategoryRepository categoryRepository, YapeRepository yapeRepository, CategoryClientRepository categoryClientRepository) {
        this.categoryRepository = categoryRepository;
        this.yapeRepository = yapeRepository;
        this.categoryClientRepository = categoryClientRepository;
    }

    @Override
    @Transactional
    public MessageResponse createCategory(CategoryDto categoryDto, Long idUser) {
        CategoryEntity categoryEntity = new CategoryEntity();
        MessageResponse messageResponse = new MessageResponse();
        if(categoryDto != null) {
            categoryEntity = CategoryMapper.mapDtoToEntity(categoryDto);
            boolean exist = categoryRepository.existsByName(categoryEntity.getName());

            CategoryEntity categoryCreated = null;
            if(!exist) {
                System.out.println("No existe");
                categoryCreated = categoryRepository.save(categoryEntity);
            } else {
                System.out.println("EXISTE");
                categoryCreated = categoryRepository.findByName(categoryEntity.getName());
            }
            CategoryClientEntity categoryClientEntity = new CategoryClientEntity();
            categoryClientEntity.setCategoryId(categoryCreated.getId());
            categoryClientEntity.setClientId(idUser);
            categoryClientRepository.save(categoryClientEntity);

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
        Page<CategoryDtoInter> categories = categoryClientRepository.findCategoriesByUserWithPagination(userId, name, pageable);
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

}
