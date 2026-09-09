package com.example.ventas_bodega.repository;
;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ProductGeneralRepository extends JpaRepository<ProductGeneralEntity, Long> {

    boolean existsByBarcode(String barcode);
    ProductGeneralEntity findByBarcode(String barcode);

    // Mantenimiento > Productos generales (SUPER_ADMIN): listado paginado del
    // catálogo compartido entre empresas (tb_producto_general no tiene company_id).
    @Query("""
    SELECT p FROM ProductGeneralEntity p
    WHERE (
            :searchKey IS NULL
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(p.category) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR p.barcode LIKE CONCAT('%', :searchKey, '%')
      )
    ORDER BY p.name ASC
    """)
    Page<ProductGeneralEntity> findAllWithFilters(
            @Param("searchKey") String searchKey,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO tb_producto_general
    (
        codigo_barras,
        nombre,
        descripcion,
        precio,
        categoria,
        imagen_url,
        imagen_url_medium,
        imagen_url_thumb
    )
    VALUES
    (
        :barcode,
        :name,
        :description,
        :price,
        :category,
        :imageUrl,
        :imageUrlMedium,
        :imageUrlThumb
    )
    ON DUPLICATE KEY UPDATE
        id_producto = id_producto
    """, nativeQuery = true)
    void insertIfNotExists(
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("description") String description,
            @Param("price") BigDecimal price,
            @Param("category") String category,
            @Param("imageUrl") String imageUrl,
            @Param("imageUrlMedium") String imageUrlMedium,
            @Param("imageUrlThumb") String imageUrlThumb
    );
}
