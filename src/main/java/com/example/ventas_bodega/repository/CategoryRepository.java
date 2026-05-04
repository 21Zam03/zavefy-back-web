package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    public boolean existsByName(String name);
    public CategoryEntity findByName(String name);
}
