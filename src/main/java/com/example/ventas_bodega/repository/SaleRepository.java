package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM SaleEntity v WHERE v.caja.id = :cajaId AND LOWER(v.paymentMethod) = 'efectivo'")
    BigDecimal sumTotalEfectivoByCajaId(@Param("cajaId") Long cajaId);

    @Query("""
        SELECT s
           FROM SaleEntity s
            WHERE s.user.company.ruc = :ruc
            AND (:type IS NULL OR s.type = :type)
            AND (:serial IS NULL OR s.serial = :serial)
            AND (:number IS NULL OR s.number = :number)
            AND (:fromDate IS NULL OR s.registerDate >= :fromDate)
            AND (:toDate IS NULL OR s.registerDate <= :toDate)
        """)
    Page<SaleEntity> findSalesByCompany(
            @Param("ruc") String ruc,
            @Param("type") String type,
            @Param("serial") String serial,
            @Param("number") Integer number,
            @Param("fromDate")String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

    @Query("""
        SELECT s
           FROM SaleEntity s
            WHERE s.user.company.ruc = :ruc
            AND (:type IS NULL OR s.type = :type)
            AND (:serial IS NULL OR s.serial = :serial)
            AND (:number IS NULL OR s.number = :number)
            AND (:fromDate IS NULL OR s.registerDate >= :fromDate)
            AND (:toDate IS NULL OR s.registerDate <= :toDate)
        """)
    List<SaleEntity> findSalesByCompany(
            @Param("ruc") String ruc,
            @Param("type") String type,
            @Param("serial") String serial,
            @Param("number") Integer number,
            @Param("fromDate")String fromDate,
            @Param("toDate") String toDate
    );

    @Query("""
            SELECT MAX(s.number)
            FROM SaleEntity s
            WHERE s.type = :type
            AND s.serial = :serial
            AND s.user.company.ruc = :ruc
    """)
    Integer findMaxNumber(
            @Param("type") String type,
            @Param("serial") String serial,
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT COALESCE(SUM(s.monto_total), 0)
    FROM tb_venta s
    INNER JOIN tb_usuario u ON s.id_usuario = u.id_usuario
    INNER JOIN tb_empresa c ON u.id_empresa = c.id_empresa
    WHERE c.ruc = :ruc
    AND s.fecha_registro BETWEEN :start AND :end
""", nativeQuery = true)
    BigDecimal getTotalSalesBetweenDates(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT COUNT(s.id_venta)
    FROM tb_venta s
    INNER JOIN tb_usuario u ON s.id_usuario = u.id_usuario
    INNER JOIN tb_empresa c ON u.id_empresa = c.id_empresa
    WHERE c.ruc = :ruc
    AND s.fecha_registro BETWEEN :start AND :end
""", nativeQuery = true)
    Long countSalesBetweenDates(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT COALESCE(SUM(d.cantidad), 0)
    FROM tb_detalle_venta d
    INNER JOIN tb_venta v ON v.id_venta = d.id_venta
    INNER JOIN tb_usuario u ON v.id_usuario = u.id_usuario
    INNER JOIN tb_empresa c ON u.id_empresa = c.id_empresa
    WHERE c.ruc = :ruc
    AND v.fecha_registro BETWEEN :start AND :end
""", nativeQuery = true)
    Long countProductsBetweenDates(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT COALESCE(AVG(v.monto_total), 0)
    FROM tb_venta v
    INNER JOIN tb_usuario u ON v.id_usuario = u.id_usuario
    INNER JOIN tb_empresa c ON u.id_empresa = c.id_empresa
    WHERE c.ruc = :ruc
    AND v.fecha_creacion BETWEEN :start AND :end
""", nativeQuery = true)
    BigDecimal getAverageTicketNative(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );

    @Query(value = """
    SELECT 
        fechas.fecha,
        COALESCE(SUM(
            CASE 
                WHEN c.ruc = :ruc THEN v.monto_total
                ELSE 0
            END
        ), 0) AS total
    FROM (
        SELECT DATE(:start) + INTERVAL (t.n) DAY AS fecha
        FROM (
            SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
            UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14
            UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19
            UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24
            UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL SELECT 29
            UNION ALL SELECT 30
        ) t
        WHERE DATE(:start) + INTERVAL t.n DAY <= DATE(:end)
    ) fechas
    LEFT JOIN tb_venta v 
        ON DATE(v.fecha_creacion) = fechas.fecha
    LEFT JOIN tb_usuario u 
        ON v.id_usuario = u.id_usuario
    LEFT JOIN tb_empresa c 
        ON u.id_empresa = c.id_empresa
    GROUP BY fechas.fecha
    ORDER BY fechas.fecha
""", nativeQuery = true)
    List<Object[]> getSalesByDayWithZeros(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );

    @Query(value = """
        SELECT 
            p.nombre AS producto,
            SUM(d.cantidad) AS total_vendidos,
            SUM(d.cantidad * d.precio_unitario) AS total_ingresos
        FROM tb_detalle_venta d
        INNER JOIN tb_venta v ON d.id_venta = v.id_venta
        INNER JOIN tb_producto p ON d.id_producto = p.id_producto
        INNER JOIN tb_usuario u ON v.id_usuario = u.id_usuario
        INNER JOIN tb_empresa c ON u.id_empresa = c.id_empresa
        WHERE c.ruc = :ruc
        AND v.fecha_registro BETWEEN :start AND :end
        GROUP BY p.nombre
        ORDER BY total_vendidos DESC
            LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getTopProducts(
            @Param("start") String start,
            @Param("end") String end,
            @Param("ruc") String ruc
    );


}
