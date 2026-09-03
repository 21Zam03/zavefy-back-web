package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.dto.PurchaseItemDto;
import com.example.ventas_bodega.dto.interfaces.PurchaseDtoInter;
import com.example.ventas_bodega.entity.PurchaseEntity;
import com.example.ventas_bodega.entity.PurchaseItemEntity;

import java.util.ArrayList;
import java.util.List;

public class PurchaseMapper {

    public static PurchaseDto mapInterfaceToDto(PurchaseDtoInter purchaseDtoInter) {
        if (purchaseDtoInter == null) {
            return null;
        }
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setPurchaseId(purchaseDtoInter.getPurchaseId());
        purchaseDto.setSupplierId(purchaseDtoInter.getSupplierId());
        purchaseDto.setSupplierName(purchaseDtoInter.getSupplierName());
        purchaseDto.setPurchaseDate(purchaseDtoInter.getPurchaseDate());
        purchaseDto.setReference(purchaseDtoInter.getReference());
        purchaseDto.setTotal(purchaseDtoInter.getTotal());
        purchaseDto.setItemCount(purchaseDtoInter.getItemCount() == null ? null : purchaseDtoInter.getItemCount().intValue());
        return purchaseDto;
    }

    public static PurchaseDto entityToDto(PurchaseEntity purchaseEntity, String supplierName, List<PurchaseItemDto> items) {
        if (purchaseEntity == null) {
            return null;
        }
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setPurchaseId(purchaseEntity.getPurchaseId());
        purchaseDto.setSupplierId(purchaseEntity.getSupplierId());
        purchaseDto.setSupplierName(supplierName);
        purchaseDto.setPurchaseDate(purchaseEntity.getPurchaseDate());
        purchaseDto.setReference(purchaseEntity.getReference());
        purchaseDto.setNotes(purchaseEntity.getNotes());
        purchaseDto.setTotal(purchaseEntity.getTotal());
        purchaseDto.setCompanyId(purchaseEntity.getCompanyId());
        purchaseDto.setCreatedBy(purchaseEntity.getCreatedBy());
        purchaseDto.setItems(items);
        purchaseDto.setItemCount(items == null ? null : items.size());
        return purchaseDto;
    }

    public static PurchaseEntity dtoToEntity(PurchaseDto purchaseDto) {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setSupplierId(purchaseDto.getSupplierId());
        purchaseEntity.setPurchaseDate(purchaseDto.getPurchaseDate());
        purchaseEntity.setReference(purchaseDto.getReference());
        purchaseEntity.setNotes(purchaseDto.getNotes());
        purchaseEntity.setTotal(purchaseDto.getTotal());
        return purchaseEntity;
    }

    public static PurchaseItemEntity itemDtoToEntity(PurchaseItemDto purchaseItemDto) {
        PurchaseItemEntity purchaseItemEntity = new PurchaseItemEntity();
        purchaseItemEntity.setProductId(purchaseItemDto.getProductId());
        purchaseItemEntity.setQuantity(purchaseItemDto.getQuantity());
        purchaseItemEntity.setCost(purchaseItemDto.getCost());
        purchaseItemEntity.setTotalCost(purchaseItemDto.getTotalCost());
        return purchaseItemEntity;
    }

    public static PurchaseItemDto itemEntityToDto(PurchaseItemEntity purchaseItemEntity) {
        PurchaseItemDto purchaseItemDto = new PurchaseItemDto();
        purchaseItemDto.setPurchaseItemId(purchaseItemEntity.getPurchaseItemId());
        purchaseItemDto.setProductId(purchaseItemEntity.getProductId());
        purchaseItemDto.setQuantity(purchaseItemEntity.getQuantity());
        purchaseItemDto.setCost(purchaseItemEntity.getCost());
        purchaseItemDto.setTotalCost(purchaseItemEntity.getTotalCost());
        return purchaseItemDto;
    }

    public static List<PurchaseItemDto> itemEntityListToDtoList(List<PurchaseItemEntity> purchaseItemEntityList) {
        List<PurchaseItemDto> purchaseItemDtoList = new ArrayList<>();
        for (PurchaseItemEntity purchaseItemEntity : purchaseItemEntityList) {
            purchaseItemDtoList.add(itemEntityToDto(purchaseItemEntity));
        }
        return purchaseItemDtoList;
    }

}
