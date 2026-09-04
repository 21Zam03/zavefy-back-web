package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
    Optional<PermissionEntity> findByName(String name);
}
