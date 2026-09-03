package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PurchaseItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, Long> {

    List<PurchaseItemEntity> findByPurchaseEntity_PurchaseId(Long purchaseId);

}
