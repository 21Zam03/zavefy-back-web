package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.*;
import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.enums.BillingPeriodEnum;
import com.example.ventas_bodega.enums.SubscriptionStatusEnum;
import com.example.ventas_bodega.exceptions.NotFoundException;
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
    private final RoleRepository roleRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public MaintenanceServiceImpl(
            CategoryRepository categoryRepository,
            YapeRepository yapeRepository,
            CategoryClientRepository categoryClientRepository,
            MeasurementUnitRepository measurementUnitRepository,
            CompanyRepository companyRepository,
            UserRepository userRepository, ProductRepository productRepository,
            ClientRepository clientRepository, MessageSourceAware messageSourceAware,
            RoleRepository roleRepository, PlanRepository planRepository,
            SubscriptionRepository subscriptionRepository) {
        this.categoryRepository = categoryRepository;
        this.yapeRepository = yapeRepository;
        this.categoryClientRepository = categoryClientRepository;
        this.measurementUnitRepository = measurementUnitRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.messageSourceAware = messageSourceAware;
        this.roleRepository = roleRepository;
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
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
    public List<CategoryDto> getCategories(Long companyId) {
        List<CategoryDtoInter> categories = categoryClientRepository.findCategoriesByUser(companyId);
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
    public MessageResponse createCompany(CompanyDto companyDto, UserDto userDto, UserEntity user, boolean isTest, String role, Long planId, String subscriptionStatus) {
        MessageResponse messageResponse = new MessageResponse();
        try {

            CompanyEntity companyEntity = CompanyMapper.dtoToEntity(companyDto);
            // CompanyMapper/CompanyDto no cargan isTest desde el request (viene aparte, como
            // parámetro de este método) ni isActive (no existe en el DTO): sin esto, toda
            // empresa nueva nacía inactiva por el valor por defecto de un boolean.
            companyEntity.setTest(isTest);
            companyEntity.setActive(true);
            CompanyEntity companyCreated = companyRepository.save(companyEntity);

            UserEntity userEntity = UserMapper.dtoToEntity(userDto);

            RoleEntity adminRole = roleRepository.findByName("Administrador")
                    .orElseThrow(() -> new NotFoundException("No se encontró el rol Administrador. Verifica que el catálogo de roles esté inicializado."));
            userEntity.setRoleList(Set.of(adminRole));

            userEntity.setCompany(companyCreated);
            userEntity.setCreatedBy(Long.valueOf(user.getUserId()));
            userEntity.setEnabled(true);
            userEntity.setAccountExpired(false);
            userEntity.setAccountLocked(false);
            userEntity.setCredentialExpired(false);
            userEntity.setPasswordReset(true);
            userEntity.setPasswordUpdateDate(LocalDateTime.now());
            userRepository.save(userEntity);

            // Suscripción inicial: opcional porque el modal de creación puede fallar al cargar
            // los planes (usa un fallback de solo UI en ese caso, sin id real que enviar).
            if (planId != null) {
                planRepository.findById(planId).ifPresent(plan -> {
                    SubscriptionEntity subscription = new SubscriptionEntity();
                    subscription.setCompany(companyCreated);
                    subscription.setPlan(plan);
                    subscription.setStatus(parseSubscriptionStatus(subscriptionStatus));
                    LocalDateTime now = LocalDateTime.now();
                    subscription.setStartDate(now);
                    subscription.setPrice(plan.getPrice());
                    subscription.setNextPaymentDate(
                            plan.getBillingPeriod() == BillingPeriodEnum.YEARLY ? now.plusYears(1) : now.plusMonths(1)
                    );
                    subscriptionRepository.save(subscription);
                });
            }

            messageResponse.setStatus(true);
            messageResponse.setMessage("Empresa creada con exitosamente");
            return messageResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    private SubscriptionStatusEnum parseSubscriptionStatus(String subscriptionStatus) {
        try {
            return SubscriptionStatusEnum.valueOf(subscriptionStatus);
        } catch (Exception e) {
            return SubscriptionStatusEnum.TRIAL;
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

    @Override
    public Page<GlobalCompanyDto> getAllCompanies(String searchKey, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyEntity> companies = companyRepository.findAllWithFilters(searchKey, active, pageable);

        List<Long> companyIds = companies.getContent().stream()
                .map(CompanyEntity::getCompanyId)
                .collect(java.util.stream.Collectors.toList());
        Map<Long, Long> usersCountByCompany = userRepository.countUsersByCompanyIds(companyIds).stream()
                .collect(java.util.stream.Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        // Una empresa hoy solo tiene la suscripción creada al momento del alta (no hay flujo
        // de cambio de plan todavía), así que la de mayor id por empresa es siempre la vigente.
        Map<Long, SubscriptionEntity> subscriptionByCompany = subscriptionRepository.findByCompany_CompanyIdIn(companyIds).stream()
                .collect(java.util.stream.Collectors.toMap(
                        s -> s.getCompany().getCompanyId(),
                        s -> s,
                        (a, b) -> a.getSubscriptionId() > b.getSubscriptionId() ? a : b
                ));

        return companies.map(company ->
                GlobalCompanyMapper.entityToDto(
                        company,
                        usersCountByCompany.getOrDefault(company.getCompanyId(), 0L),
                        subscriptionByCompany.get(company.getCompanyId())
                )
        );
    }

    @Override
    public MessageResponse activateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("La empresa no existe"));
        company.setActive(true);
        companyRepository.save(company);
        return new MessageResponse("Empresa activada", true);
    }

    @Override
    public MessageResponse deactivateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("La empresa no existe"));
        company.setActive(false);
        companyRepository.save(company);
        return new MessageResponse("Empresa desactivada", true);
    }

}
