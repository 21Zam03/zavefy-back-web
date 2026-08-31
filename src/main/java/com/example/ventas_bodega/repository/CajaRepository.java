package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CajaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CajaRepository extends JpaRepository<CajaEntity, Long> {

    Optional<CajaEntity> findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(String ruc);
    Page<CajaEntity> findByUser_Company_RucAndFechaCierreIsNotNullOrderByFechaCierreDesc(String ruc, Pageable pageable);

}
