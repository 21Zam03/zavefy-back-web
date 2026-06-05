package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.*;
import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.mapper.*;
import com.example.ventas_bodega.repository.*;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MessageSourceAware messageSourceAware;
    private CategoryRepository categoryRepository;
    private YapeRepository yapeRepository;
    private CategoryClientRepository categoryClientRepository;
    private MeasurementUnitRepository measurementUnitRepository;
    private CompanyRepository companyRepository;
    private ClientRepository clientRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public MaintenanceServiceImpl(
            CategoryRepository categoryRepository,
            YapeRepository yapeRepository,
            CategoryClientRepository categoryClientRepository,
            MeasurementUnitRepository measurementUnitRepository,
            CompanyRepository companyRepository,
            UserRepository userRepository, ProductRepository productRepository,
            ClientRepository clientRepository, MessageSourceAware messageSourceAware) {
        this.categoryRepository = categoryRepository;
        this.yapeRepository = yapeRepository;
        this.categoryClientRepository = categoryClientRepository;
        this.measurementUnitRepository = measurementUnitRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.messageSourceAware = messageSourceAware;
    }

    @Override
    @Transactional
    public MessageResponse createCategory(CategoryDto categoryDto, Long companyId) {
        CategoryEntity categoryEntity = new CategoryEntity();
        MessageResponse messageResponse = new MessageResponse();
        if(categoryDto != null) {
            if(categoryRepository.existsByNameAndCompanyId(categoryDto.getName(), companyId)) {
                messageResponse.setStatus(false);
                messageResponse.setMessage("Ya existe una categoria con el nombre: "+categoryDto.getName());
                return messageResponse;
            }
            categoryEntity = CategoryMapper.mapDtoToEntity(categoryDto);
            categoryEntity.setCompanyId(companyId);
            categoryEntity.setActive(true);
            categoryRepository.save(categoryEntity);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Categoria creada exitosamente");
        } else {
            messageResponse.setStatus(false);
            messageResponse.setMessage("Ocurrio un error al crear la categoria");
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteCategory(Long idCategory, UserEntity user) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            boolean exist = productRepository.existsProductsByCategoryAndCompany(idCategory, user.getCompany().getCompanyId()) == 1;
            if(exist) {
                //NO se puede eliminar
                messageResponse.setStatus(false);
                messageResponse.setMessage("No se puede eliminar una categoria que ya ha sido usada en un producto");
                return messageResponse;
            } else {
                //categoryRepository.deactivateCategory(idCategory, user.getCompany().getCompanyId());
                categoryRepository.deleteById(idCategory);
                messageResponse.setStatus(true);
                messageResponse.setMessage("Categoria eliminada exitosamente");
                return messageResponse;
            }
        } catch (Exception ex) {
            messageResponse.setStatus(false);
            messageResponse.setMessage(ex.getMessage());
            return messageResponse;
        }
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
    public MessageResponse updateCategory(CategoryDto categoryDto, Long UserId) {
        MessageResponse messageResponse = new MessageResponse();
        Optional<CategoryEntity> categoryToUpdate = categoryRepository.findById(categoryDto.getId());
        if(categoryToUpdate.isPresent()) {
            CategoryEntity categoryEntity = categoryToUpdate.get();
            categoryEntity.setName(categoryDto.getName());

            categoryRepository.save(categoryEntity);
            messageResponse.setStatus(true);
            messageResponse.setMessage("Categoria actualizada exitosamente");
        } else {
            messageResponse.setStatus(false);
            messageResponse.setMessage("Ocurrio un erro al crear la categoria");
        }
        return messageResponse;
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
    public MessageResponse createCompany(CompanyDto companyDto, UserDto userDto, UserEntity user, boolean isTest, String role) {
        MessageResponse messageResponse = new MessageResponse();
        try {

            CompanyEntity companyEntity = CompanyMapper.dtoToEntity(companyDto);
            CompanyEntity companyCreated = companyRepository.save(companyEntity);

            UserEntity userEntity = UserMapper.dtoToEntity(userDto);
            PermissionEntity permissionEntity = new  PermissionEntity();
            //Set<PermissionEntity> permissionList = permissionRepository.findAll();


            RoleEntity roleEntity = new RoleEntity();
            Set<RoleEntity> roleList = new HashSet<>();


            userEntity.setCompany(companyCreated);
            userEntity.setCreatedBy(Long.valueOf(user.getUserId()));
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

    @Override
    public Page<ClientDto> getClientsByCompany(UserEntity user, String searchKey, Boolean active, String documentType, String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientDtoInter> clients = clientRepository.findClientsByFilters(user.getCompany().getCompanyId(), searchKey, active, documentType, fromDate, toDate, pageable);
        List<ClientDto> data = new ArrayList<>();
        for (int i = 0; i<clients.getContent().size(); i++) {
            data.add(ClientMapper.mapIntefaceToDto(clients.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, clients.getTotalElements());
    }

    @Override
    public MessageResponse createClient(ClientDto clientDto, UserEntity user) {
        ClientEntity clientEntity = new ClientEntity();
        MessageResponse messageResponse = new MessageResponse();
        if(clientDto != null) {
            clientEntity = ClientMapper.dtoToEntity(clientDto);
            clientEntity.setEnabled(true);
            clientEntity.setCompanyId(user.getCompany().getCompanyId());
            ClientEntity clientCreated = clientRepository.save(clientEntity);
            messageResponse.setClientDto(ClientMapper.entityToDto(clientCreated));
            messageResponse.setStatus(true);
            messageResponse.setMessage("Cliente creada con exitosamente");
        } else {
            messageResponse.setStatus(false);
            messageResponse.setMessage("Ocurrio un error al crear la cliente");
        }
        return messageResponse;
    }

}
