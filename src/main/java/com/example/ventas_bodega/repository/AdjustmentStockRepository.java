package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.AdjustmentStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjustmentStockRepository extends JpaRepository<AdjustmentStockEntity, Long> {
}
