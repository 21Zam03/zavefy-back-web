package com.example.ventas_bodega.repository;
;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ProductGeneralRepository extends JpaRepository<ProductGeneralEntity, Long> {

    boolean existsByBarcode(String barcode);
    ProductGeneralEntity findByBarcode(String barcode);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO tb_producto_general
    (
        codigo_barras,
        nombre,
        descripcion,
        precio,
        categorias,
        imagen_url
    )
    VALUES
    (
        :barcode,
        :name,
        :description,
        :price,
        :categories,
        :imageUrl
    )
    ON DUPLICATE KEY UPDATE
        id_producto = id_producto
    """, nativeQuery = true)
    void insertIfNotExists(
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("description") String description,
            @Param("price") BigDecimal price,
            @Param("categories") String categories,
            @Param("imageUrl") String imageUrl
    );
}
