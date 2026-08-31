package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CajaMovimientoEntity;
import com.example.ventas_bodega.enums.CajaMovimientoTipoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CajaMovimientoRepository extends JpaRepository<CajaMovimientoEntity, Long> {

    List<CajaMovimientoEntity> findByCaja_IdOrderByFechaDesc(Long cajaId);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM CajaMovimientoEntity m WHERE m.caja.id = :cajaId AND m.tipo = :tipo")
    BigDecimal sumMontoByCajaIdAndTipo(@Param("cajaId") Long cajaId, @Param("tipo") CajaMovimientoTipoEnum tipo);

}
