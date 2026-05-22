package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    public boolean existsByName(String name);
    public CategoryEntity findByName(String name);

    @Query(value = """
        SELECT c.id_categoria, c.nombre
        FROM tb_categoria c
        WHERE c.id_empresa = :companyId
          AND c.activo = true
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM  tb_categoria c 
        WHERE c.id_empresa = :companyId
          AND c.activo = true
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        """,
            nativeQuery = true)
    Page<CategoryDtoInter> findCategoriesByUserWithPagination(
            @Param("companyId") Long companyId,
            @Param("nombre") String nombre,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE tb_categoria
    SET activo = false
    WHERE id_categoria = :categoryId
    AND id_empresa = :companyId
    """, nativeQuery = true)
    void deactivateCategory(
            @Param("categoryId") Long categoryId,
            @Param("companyId") Long companyId
    );


}
