package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Long, CartEntity> {



}
