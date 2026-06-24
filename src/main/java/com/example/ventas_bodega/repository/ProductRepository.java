package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByBarcodeAndCompany_RucAndActive(String barcode, String ruc, boolean active);
    boolean existsByBarcodeAndCompany_Ruc(String barcode, String ruc);
    Page<ProductEntity> findByCompany_Ruc(String ruc, Pageable pageable);
    List<ProductEntity> findByCompany_Ruc(String ruc);

    @Query(value = """
    SELECT DISTINCT p.*
    FROM tb_producto p
    INNER JOIN tb_empresa e ON p.id_empresa = e.id_empresa
    LEFT JOIN tb_categorias_productos cp ON cp.id_producto = p.id_producto
    WHERE e.ruc = :ruc
    AND (:barcode IS NULL OR p.codigo_barras = :barcode)
    AND (:name IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR cp.id_categoria = :category)
    AND (
        :stockStatus IS NULL
        OR (:stockStatus = 'SIN_STOCK' AND p.stock = 0)
        OR (:stockStatus = 'BAJO_STOCK' AND p.stock BETWEEN 1 AND 10)
        OR (:stockStatus = 'STOCK_MODERADO' AND p.stock BETWEEN 11 AND 50)
        OR (:stockStatus = 'STOCK_SUFICIENTE' AND p.stock > 50)
    )
    AND (:active IS NULL OR p.activo = :active)
    ORDER BY p.fecha_actualizacion DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.id_producto)
    FROM tb_producto p
    INNER JOIN tb_empresa e ON p.id_empresa = e.id_empresa
    LEFT JOIN tb_categorias_productos cp ON cp.id_producto = p.id_producto
    WHERE e.ruc = :ruc
    AND (:barcode IS NULL OR p.codigo_barras = :barcode)
    AND (:name IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR cp.id_categoria = :category)
    AND (
        :stockStatus IS NULL
        OR (:stockStatus = 'SIN_STOCK' AND p.stock = 0)
        OR (:stockStatus = 'BAJO_STOCK' AND p.stock BETWEEN 1 AND 10)
        OR (:stockStatus = 'STOCK_MODERADO' AND p.stock BETWEEN 11 AND 50)
        OR (:stockStatus = 'STOCK_SUFICIENTE' AND p.stock > 50)
    )
    AND (:active IS NULL OR p.activo = :active)
    """, nativeQuery = true
    )
    Page<ProductEntity> findProductsByCompanyAndBarcode(
            @Param("ruc") String ruc,
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("stockStatus") String stockStatus,
            @Param("active") Boolean active,
            @Param("category") Long categoryId,
            Pageable pageable
    );

    @Query(value = """
    SELECT DISTINCT p.*
    FROM tb_producto p
    INNER JOIN tb_empresa e ON p.id_empresa = e.id_empresa
    LEFT JOIN tb_categorias_productos cp ON cp.id_producto = p.id_producto
    WHERE e.id_empresa = :id
    AND (:barcode IS NULL OR p.codigo_barras = :barcode)
    AND (:name IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR cp.id_categoria = :category)
    AND (
        :stockStatus IS NULL
        OR (:stockStatus = 'SIN_STOCK' AND p.stock = 0)
        OR (:stockStatus = 'BAJO_STOCK' AND p.stock BETWEEN 1 AND 10)
        OR (:stockStatus = 'STOCK_MODERADO' AND p.stock BETWEEN 11 AND 50)
        OR (:stockStatus = 'STOCK_SUFICIENTE' AND p.stock > 50)
    )
    AND (:active IS NULL OR p.activo = :active)
    ORDER BY p.fecha_actualizacion DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.id_producto)
    FROM tb_producto p
    INNER JOIN tb_empresa e ON p.id_empresa = e.id_empresa
    LEFT JOIN tb_categorias_productos cp ON cp.id_producto = p.id_producto
    WHERE e.id_empresa = :id
    AND (:barcode IS NULL OR p.codigo_barras = :barcode)
    AND (:name IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR cp.id_categoria = :category)
    AND (
        :stockStatus IS NULL
        OR (:stockStatus = 'SIN_STOCK' AND p.stock = 0)
        OR (:stockStatus = 'BAJO_STOCK' AND p.stock BETWEEN 1 AND 10)
        OR (:stockStatus = 'STOCK_MODERADO' AND p.stock BETWEEN 11 AND 50)
        OR (:stockStatus = 'STOCK_SUFICIENTE' AND p.stock > 50)
    )
    AND (:active IS NULL OR p.activo = :active)
    """, nativeQuery = true
    )
    Page<ProductEntity> findProductsFromCompanyId(
            @Param("id") Long id,
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("stockStatus") String stockStatus,
            @Param("active") Boolean active,
            @Param("category") Long categoryId,
            Pageable pageable
    );

    @Query(value = """
    SELECT DISTINCT p.*
    FROM tb_producto p
    INNER JOIN tb_empresa e ON p.id_empresa = e.id_empresa
    LEFT JOIN tb_categorias_productos cp ON cp.id_producto = p.id_producto
    WHERE e.id_empresa = :id
    AND (:barcode IS NULL OR p.codigo_barras = :barcode)
    AND (:name IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:category IS NULL OR cp.id_categoria = :category)
    AND (
        :stockStatus IS NULL
        OR (:stockStatus = 'SIN_STOCK' AND p.stock = 0)
        OR (:stockStatus = 'BAJO_STOCK' AND p.stock BETWEEN 1 AND 10)
        OR (:stockStatus = 'STOCK_MODERADO' AND p.stock BETWEEN 11 AND 50)
        OR (:stockStatus = 'STOCK_SUFICIENTE' AND p.stock > 50)
    )
    AND (:active IS NULL OR p.activo = :active)
    ORDER BY p.fecha_actualizacion DESC
    """,
            nativeQuery = true
    )
    List<ProductEntity> findProductsFromCompanyId(
            @Param("id") Long id,
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("stockStatus") String stockStatus,
            @Param("active") Boolean active,
            @Param("category") Long categoryId
    );

    @Query(
            value = """
        SELECT DISTINCT p.*
        FROM tb_producto p
        INNER JOIN tb_empresa e 
            ON p.id_empresa = e.id_empresa
        WHERE e.ruc = :ruc
        AND (
            LOWER(p.nombre) = LOWER(:search)
            OR p.codigo_barras = :search
        )
        ORDER BY p.fecha_actualizacion DESC
        """,
            nativeQuery = true
    )
    ProductEntity searchProductsByCompanyForSaleModule(
            @Param("ruc") String ruc,
            @Param("search") String search
    );

    @Modifying
    @Query("UPDATE ProductEntity p SET p.barcode = :barcode WHERE p.id = :id")
    void updateBarcodeById(@Param("id") Long id, @Param("barcode") String barcode);

    @Modifying
    @Transactional
    @Query("""
       UPDATE ProductEntity p
       SET p.active = false
       WHERE p.id = :productId
       AND p.company.id = :companyId
       """)
    void deactivateProduct(@Param("productId") Long productId,
                           @Param("companyId") Long companyId);

    @Modifying
    @Transactional
    @Query("""
       UPDATE ProductEntity p
       SET p.active = true
       WHERE p.id = :productId
       AND p.company.id = :companyId
       """)
    void activateProduct(@Param("productId") Long productId,
                           @Param("companyId") Long companyId);

    @Modifying
    @Query("""
    UPDATE ProductEntity p
    SET p.imageUrl = :imageUrl,
        p.filePath = :filePath
    WHERE p.id = :productId
    AND p.company.id = :companyId
""")
    void updateImageInfo(
            @Param("imageUrl") String imageUrl,
            @Param("filePath") String filePath,
            @Param("productId") Long  productId,
            @Param("companyId") Long companyId
    );

    @Query(value = """
    SELECT 
        p.nombre,
        p.stock,
        CASE 
            WHEN p.stock = 0 THEN 'SIN STOCK'
            WHEN p.stock <= 2 THEN 'CRITICO'
            WHEN p.stock <= 5 THEN 'BAJO'
            ELSE 'NORMAL'
        END AS estado
    FROM tb_producto p
    INNER JOIN tb_empresa c ON p.id_empresa = c.id_empresa
    WHERE c.ruc = :ruc
    AND p.activo = true
    ORDER BY p.stock ASC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> getTopLowStockAlerts(
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM tb_producto
        WHERE id_categoria = :categoryId
        AND id_empresa = :companyId
    )
    """, nativeQuery = true)
    Integer existsProductsByCategoryAndCompany(
            @Param("categoryId") Long categoryId,
            @Param("companyId") Long companyId
    );

}

