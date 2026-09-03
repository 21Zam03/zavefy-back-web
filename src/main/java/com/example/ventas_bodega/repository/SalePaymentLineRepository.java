package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.SalePaymentLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalePaymentLineRepository extends JpaRepository<SalePaymentLineEntity, Long> {

    List<SalePaymentLineEntity> findBySaleEntity_VentaId(Integer ventaId);

}
