package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface PurchaseService {

    Page<PurchaseDto> getPurchasesByCompany(UserEntity user, String searchKey, Long supplierId, String fromDate, String toDate, int page, int size);
    PurchaseDto getPurchaseById(Long purchaseId, UserEntity user);
    MessageResponse createPurchase(PurchaseDto purchaseDto, UserEntity user);

}
