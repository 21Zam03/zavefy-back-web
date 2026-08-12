package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.CategoryDtoInter;
import com.example.ventas_bodega.entity.CategoryClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryClientRepository extends JpaRepository<CategoryClientEntity, Long> {

    @Query(value = """
        SELECT c.id_categoria, c.nombre
        FROM tb_categoria c
        WHERE c.id_empresa = :companyId
          AND c.activo = true
        """, nativeQuery = true)
    List<CategoryDtoInter> findCategoriesByUser(@Param("companyId") Long companyId);

    @Query(value = """
        SELECT c.id_categoria, c.nombre
        FROM tb_categoria_cliente cc
        JOIN tb_categoria c ON c.id_categoria = cc.id_categoria
        WHERE cc.id_cliente = :userId
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_categoria_cliente cc
        JOIN tb_categoria c ON c.id_categoria = cc.id_categoria
        WHERE cc.id_cliente = :userId
          AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        """,
            nativeQuery = true)
    Page<CategoryDtoInter> findCategoriesByUserWithPagination(
            @Param("userId") Long userId,
            @Param("nombre") String nombre,
            Pageable pageable
    );


}
