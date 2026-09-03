package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.dto.PurchaseItemDto;
import com.example.ventas_bodega.dto.interfaces.PurchaseDtoInter;
import com.example.ventas_bodega.entity.PurchaseEntity;
import com.example.ventas_bodega.entity.PurchaseItemEntity;
import com.example.ventas_bodega.entity.SupplierEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.PurchaseMapper;
import com.example.ventas_bodega.repository.PurchaseItemRepository;
import com.example.ventas_bodega.repository.PurchaseRepository;
import com.example.ventas_bodega.repository.SupplierRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
import com.example.ventas_bodega.service.PurchaseService;
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
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;

    @Autowired
    public PurchaseServiceImpl(
            PurchaseRepository purchaseRepository,
            PurchaseItemRepository purchaseItemRepository,
            SupplierRepository supplierRepository,
            InventoryService inventoryService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    public Page<PurchaseDto> getPurchasesByCompany(UserEntity user, String searchKey, Long supplierId, String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseDtoInter> purchases = purchaseRepository.findPurchasesByFilters(
                user.getCompany().getCompanyId(), searchKey, supplierId, fromDate, toDate, pageable
        );
        List<PurchaseDto> data = new ArrayList<>();
        for (int i = 0; i < purchases.getContent().size(); i++) {
            data.add(PurchaseMapper.mapInterfaceToDto(purchases.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, purchases.getTotalElements());
    }

    @Override
    public PurchaseDto getPurchaseById(Long purchaseId, UserEntity user) {
        PurchaseEntity purchaseEntity = purchaseRepository.findByPurchaseIdAndCompanyId(purchaseId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("La compra no existe"));

        SupplierEntity supplierEntity = supplierRepository.findBySupplierIdAndCompanyId(purchaseEntity.getSupplierId(), user.getCompany().getCompanyId())
                .orElse(null);
        String supplierName = supplierEntity == null ? null : supplierEntity.getBusinessName();

        List<PurchaseItemEntity> items = purchaseItemRepository.findByPurchaseEntity_PurchaseId(purchaseId);

        return PurchaseMapper.entityToDto(purchaseEntity, supplierName, PurchaseMapper.itemEntityListToDtoList(items));
    }

    @Override
    @Transactional
    public MessageResponse createPurchase(PurchaseDto purchaseDto, UserEntity user) {
        if (purchaseDto == null) {
            throw new IllegalArgumentException("Información de la compra es nula");
        }
        if (purchaseDto.getSupplierId() == null) {
            throw new IllegalArgumentException("La compra no tiene proveedor");
        }
        if (purchaseDto.getItems() == null || purchaseDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("La compra no tiene productos");
        }

        SupplierEntity supplierEntity = supplierRepository.findBySupplierIdAndCompanyId(purchaseDto.getSupplierId(), user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El proveedor no existe en su inventario"));

        PurchaseEntity purchaseToCreate = PurchaseMapper.dtoToEntity(purchaseDto);
        purchaseToCreate.setCompanyId(user.getCompany().getCompanyId());
        purchaseToCreate.setCreatedBy(Long.valueOf(user.getUserId()));
        PurchaseEntity purchaseCreated = purchaseRepository.save(purchaseToCreate);

        for (PurchaseItemDto itemDto : purchaseDto.getItems()) {
            PurchaseItemEntity itemEntity = PurchaseMapper.itemDtoToEntity(itemDto);
            itemEntity.setPurchaseEntity(purchaseCreated);
            purchaseItemRepository.save(itemEntity);
        }

        MessageResponse stockResponse = inventoryService.createHistoryStockForPurchase(purchaseDto.getItems(), user, purchaseCreated.getPurchaseId());
        if (!stockResponse.isStatus()) {
            // Fuerza el rollback de TODA la compra (cabecera + detalles), no solo del stock
            throw new IllegalStateException(stockResponse.getMessage());
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setPurchaseDto(PurchaseMapper.entityToDto(purchaseCreated, supplierEntity.getBusinessName(), purchaseDto.getItems()));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Compra registrada exitosamente");
        return messageResponse;
    }

}
