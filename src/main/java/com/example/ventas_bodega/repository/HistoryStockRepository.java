package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.HistoryStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryStockRepository extends JpaRepository<HistoryStockEntity, Long> {
}
