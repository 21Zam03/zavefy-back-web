package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.HistoryStockDtoInter;
import com.example.ventas_bodega.entity.HistoryStockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoryStockRepository extends JpaRepository<HistoryStockEntity, Long> {

    @Query(value = """
    SELECT
        hs.id_historial_stock AS historyStockId,
        hs.evento AS event,
        hs.fecha_creacion AS createDate,

        hs.stock_antes AS stockBefore,
        hs.stock_despues AS stockAfter,
        hs.stock_variacion AS stockVariation,

        hs.empresa_id AS companyId,
        hs.creado_por AS createdBy,

        p.nombre AS productName,

        v.id_venta AS saleId,

        CASE
            WHEN v.id_venta IS NOT NULL
            THEN CONCAT(v.serie, '-', v.numero)
            ELSE NULL
        END AS saleName,

        u.nombres AS createdByName

    FROM tb_historial_stock hs

    INNER JOIN tb_producto p
        ON p.id_producto = hs.product_id

    LEFT JOIN tb_venta v
        ON v.id_venta = hs.venta_id

    LEFT JOIN tb_usuario u
        ON u.id_usuario = hs.creado_por

    WHERE
        hs.empresa_id = :companyId

        AND (:event IS NULL OR :event = '' OR hs.evento = :event)

        AND (
            :fromDate IS NULL
            OR :fromDate = ''
            OR DATE(hs.fecha_creacion) >= :fromDate
        )

        AND (
            :toDate IS NULL
            OR :toDate = ''
            OR DATE(hs.fecha_creacion) <= :toDate
        )

        AND (
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )

    ORDER BY hs.fecha_creacion DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM tb_historial_stock hs

    INNER JOIN tb_producto p
        ON p.id_producto = hs.product_id

    LEFT JOIN tb_venta v
        ON v.id_venta = hs.venta_id

    LEFT JOIN tb_usuario u
        ON u.id_usuario = hs.creado_por

    WHERE
        hs.empresa_id = :companyId

        AND (:event IS NULL OR :event = '' OR hs.evento = :event)

        AND (
            :fromDate IS NULL
            OR :fromDate = ''
            OR DATE(hs.fecha_creacion) >= :fromDate
        )

        AND (
            :toDate IS NULL
            OR :toDate = ''
            OR DATE(hs.fecha_creacion) <= :toDate
        )

        AND (
            :keyword IS NULL
            OR :keyword = ''
            OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """,
            nativeQuery = true)
    Page<HistoryStockDtoInter> findHistoryStockByFilters(
            @Param("companyId") Long companyId,
            @Param("keyword") String keyword,
            @Param("event") String event,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

}
