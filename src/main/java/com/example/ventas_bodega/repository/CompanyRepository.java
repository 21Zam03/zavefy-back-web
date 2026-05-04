package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    public CompanyEntity findByRuc(String ruc);

}
