package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    List<CartEntity> findByStatus(String status);

}
