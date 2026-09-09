package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.SalePaymentLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SalePaymentLineRepository extends JpaRepository<SalePaymentLineEntity, Long> {

    List<SalePaymentLineEntity> findBySaleEntity_VentaId(Integer ventaId);

    // Parte en efectivo de ventas con pago dividido (ej. mitad efectivo, mitad tarjeta):
    // SaleRepository.sumTotalEfectivoByCajaId solo mira ventas 100% efectivo, así que esta
    // consulta cubre la porción de efectivo que quedaba fuera del arqueo de caja.
    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) FROM SalePaymentLineEntity p
        WHERE p.saleEntity.caja.id = :cajaId
        AND LOWER(p.method) = 'efectivo'
        AND p.saleEntity.voided = false
    """)
    BigDecimal sumEfectivoByCajaId(@Param("cajaId") Long cajaId);

}
