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

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    public boolean existsByNameAndCompanyId(String name, Long companyId);
    Optional<CategoryEntity> findByNameIgnoreCaseAndCompanyId(String name, Long companyId);
    public List<CategoryEntity> findAllByCompanyId(Long companyId);

    @Query(value = """
        SELECT c.id_categoria, c.nombre
        FROM tb_categoria c
        WHERE c.id_empresa = :companyId
          AND c.activo = true
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
                ORDER BY c.fecha_actualizacion DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM  tb_categoria c 
        WHERE c.id_empresa = :companyId
          AND c.activo = true
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
                ORDER BY c.fecha_actualizacion DESC
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

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE tb_categoria
    SET activo = true
    WHERE id_categoria = :categoryId
    AND id_empresa = :companyId
    """, nativeQuery = true)
    void activateCategory(
            @Param("categoryId") Long categoryId,
            @Param("companyId") Long companyId
    );


}
