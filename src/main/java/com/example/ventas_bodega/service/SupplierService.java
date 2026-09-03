package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.SupplierDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface SupplierService {

    Page<SupplierDto> getSuppliersByCompany(UserEntity user, String searchKey, Boolean active, String documentType, String fromDate, String toDate, int page, int size);
    SupplierDto getSupplierById(Long supplierId, UserEntity user);
    MessageResponse createSupplier(SupplierDto supplierDto, UserEntity user);
    MessageResponse updateSupplier(SupplierDto supplierDto, UserEntity user);
    MessageResponse deactivateSupplier(Long supplierId, UserEntity user);
    MessageResponse activateSupplier(Long supplierId, UserEntity user);

}
