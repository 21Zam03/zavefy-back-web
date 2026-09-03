package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CajaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<CajaEntity, Long> {

    Optional<CajaEntity> findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(String ruc);
    Page<CajaEntity> findByUser_Company_RucAndFechaCierreIsNotNullOrderByFechaCierreDesc(String ruc, Pageable pageable);

    // Usada por el cron de notificaciones (NotificationServiceImpl): cajas abiertas hace más tiempo del umbral, de todas las empresas.
    @Query("SELECT c FROM CajaEntity c WHERE c.fechaCierre IS NULL AND c.fechaApertura <= :threshold")
    List<CajaEntity> findOpenCajasOlderThan(@Param("threshold") LocalDateTime threshold);

}
