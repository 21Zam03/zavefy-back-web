package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.*;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaintenanceService {

    public MessageResponse createCategory(CategoryDto categoryDto, Long companyId);
    public MessageResponse deleteCategory(Long idCategory, UserEntity user);
    public List<CategoryDto> getCategories(Long UserId);
    public Page<CategoryDto> getCategoriesWithPagination(Long UserId, String name, int page, int size);
    public MessageResponse updateCategory(CategoryDto categoryDto, Long UserId);
    public List<YapeDto> getYapesByCompany(Long companyId);
    public List<MeasurementUnitDto> getMeasurementUnits();
    public MessageResponse createCompany(CompanyDto companyDto, UserDto userDto, UserEntity user, boolean isTest, String role, Long planId, String subscriptionStatus);
    public Page<ClientDto> getClientsByCompany(UserEntity user, String searchKey, Boolean active, String documentType, String fromDate, String toDate, int page, int size);
    MessageResponse createClient(ClientDto clientDto, UserEntity user);

    // Mantenimiento > Empresas (SUPER_ADMIN): cruza todas las empresas del sistema.
    Page<GlobalCompanyDto> getAllCompanies(String searchKey, Boolean active, int page, int size);
    MessageResponse activateCompany(Long companyId);
    MessageResponse deactivateCompany(Long companyId);

}
