package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.SupplierDto;
import com.example.ventas_bodega.dto.interfaces.SupplierDtoInter;
import com.example.ventas_bodega.entity.SupplierEntity;

public class SupplierMapper {

    public static SupplierDto mapInterfaceToDto(SupplierDtoInter supplierDtoInter) {
        if (supplierDtoInter == null) {
            return null;
        }
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierId(supplierDtoInter.getSupplierId());
        supplierDto.setBusinessName(supplierDtoInter.getBusinessName());
        supplierDto.setContactName(supplierDtoInter.getContactName());
        supplierDto.setEmail(supplierDtoInter.getEmail());
        supplierDto.setEnabled(supplierDtoInter.getEnabled());
        supplierDto.setDocumentNumber(supplierDtoInter.getDocumentNumber());
        supplierDto.setDocumentType(supplierDtoInter.getDocumentType());
        supplierDto.setPhoneNumber(supplierDtoInter.getPhoneNumber());
        supplierDto.setAddress(supplierDtoInter.getAddress());
        supplierDto.setCreatedDate(supplierDtoInter.getCreatedDate());
        supplierDto.setUpdatedDate(supplierDtoInter.getUpdatedDate());
        return supplierDto;
    }

    public static SupplierDto entityToDto(SupplierEntity supplierEntity) {
        if (supplierEntity == null) {
            return null;
        }
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierId(supplierEntity.getSupplierId());
        supplierDto.setBusinessName(supplierEntity.getBusinessName());
        supplierDto.setContactName(supplierEntity.getContactName());
        supplierDto.setEmail(supplierEntity.getEmail());
        supplierDto.setEnabled(supplierEntity.isEnabled());
        supplierDto.setCompanyId(supplierEntity.getCompanyId());
        supplierDto.setDocumentNumber(supplierEntity.getDocumentNumber());
        supplierDto.setDocumentType(supplierEntity.getDocumentType());
        supplierDto.setPhoneNumber(supplierEntity.getPhoneNumber());
        supplierDto.setAddress(supplierEntity.getAddress());
        supplierDto.setCreatedDate(supplierEntity.getCreatedDate());
        supplierDto.setUpdatedDate(supplierEntity.getUpdatedDate());
        return supplierDto;
    }

    public static SupplierEntity dtoToEntity(SupplierDto supplierDto) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setSupplierId(supplierDto.getSupplierId());
        supplierEntity.setBusinessName(supplierDto.getBusinessName());
        supplierEntity.setContactName(supplierDto.getContactName());
        supplierEntity.setEmail(supplierDto.getEmail());
        supplierEntity.setDocumentNumber(supplierDto.getDocumentNumber());
        supplierEntity.setDocumentType(supplierDto.getDocumentType());
        supplierEntity.setPhoneNumber(supplierDto.getPhoneNumber());
        supplierEntity.setAddress(supplierDto.getAddress());
        return supplierEntity;
    }

}
