package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CartDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Long> {
}
