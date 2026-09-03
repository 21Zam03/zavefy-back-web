package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.SupplierDto;
import com.example.ventas_bodega.dto.interfaces.SupplierDtoInter;
import com.example.ventas_bodega.entity.SupplierEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.SupplierMapper;
import com.example.ventas_bodega.repository.SupplierRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Page<SupplierDto> getSuppliersByCompany(UserEntity user, String searchKey, Boolean active, String documentType, String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierDtoInter> suppliers = supplierRepository.findSuppliersByFilters(
                user.getCompany().getCompanyId(), searchKey, active, documentType, fromDate, toDate, pageable
        );
        List<SupplierDto> data = new ArrayList<>();
        for (int i = 0; i < suppliers.getContent().size(); i++) {
            data.add(SupplierMapper.mapInterfaceToDto(suppliers.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, suppliers.getTotalElements());
    }

    @Override
    public SupplierDto getSupplierById(Long supplierId, UserEntity user) {
        SupplierEntity supplierEntity = supplierRepository.findBySupplierIdAndCompanyId(supplierId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El proveedor no existe en su inventario"));
        return SupplierMapper.entityToDto(supplierEntity);
    }

    @Override
    public MessageResponse createSupplier(SupplierDto supplierDto, UserEntity user) {
        MessageResponse messageResponse = new MessageResponse();
        SupplierEntity supplierEntity = SupplierMapper.dtoToEntity(supplierDto);
        supplierEntity.setEnabled(true);
        supplierEntity.setCompanyId(user.getCompany().getCompanyId());
        SupplierEntity supplierCreated = supplierRepository.save(supplierEntity);
        messageResponse.setSupplierDto(SupplierMapper.entityToDto(supplierCreated));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Proveedor creado exitosamente");
        return messageResponse;
    }

    @Override
    public MessageResponse updateSupplier(SupplierDto supplierDto, UserEntity user) {
        SupplierEntity supplierToUpdate = supplierRepository.findBySupplierIdAndCompanyId(supplierDto.getSupplierId(), user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El proveedor no existe en su inventario"));

        supplierToUpdate.setBusinessName(supplierDto.getBusinessName());
        supplierToUpdate.setContactName(supplierDto.getContactName());
        supplierToUpdate.setEmail(supplierDto.getEmail());
        supplierToUpdate.setDocumentNumber(supplierDto.getDocumentNumber());
        supplierToUpdate.setDocumentType(supplierDto.getDocumentType());
        supplierToUpdate.setPhoneNumber(supplierDto.getPhoneNumber());
        supplierToUpdate.setAddress(supplierDto.getAddress());
        SupplierEntity supplierUpdated = supplierRepository.save(supplierToUpdate);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setSupplierDto(SupplierMapper.entityToDto(supplierUpdated));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Proveedor actualizado exitosamente");
        return messageResponse;
    }

    @Override
    public MessageResponse deactivateSupplier(Long supplierId, UserEntity user) {
        MessageResponse messageResponse = new MessageResponse();
        int updated = supplierRepository.deactivateSupplier(supplierId, user.getCompany().getCompanyId());
        if (updated == 0) {
            throw new NotFoundException("El proveedor no existe en su inventario");
        }
        messageResponse.setStatus(true);
        messageResponse.setMessage("El proveedor se ha desactivado exitosamente");
        return messageResponse;
    }

    @Override
    public MessageResponse activateSupplier(Long supplierId, UserEntity user) {
        MessageResponse messageResponse = new MessageResponse();
        int updated = supplierRepository.activateSupplier(supplierId, user.getCompany().getCompanyId());
        if (updated == 0) {
            throw new NotFoundException("El proveedor no existe en su inventario");
        }
        messageResponse.setStatus(true);
        messageResponse.setMessage("El proveedor se ha activado exitosamente");
        return messageResponse;
    }

}
