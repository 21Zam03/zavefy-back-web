package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.PurchaseDtoInter;
import com.example.ventas_bodega.entity.PurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    Optional<PurchaseEntity> findByPurchaseIdAndCompanyId(Long purchaseId, Long companyId);

    @Query(value = """
    SELECT
        c.id_compra AS purchaseId,
        c.id_proveedor AS supplierId,
        s.razon_social AS supplierName,
        c.fecha_compra AS purchaseDate,
        c.referencia AS reference,
        c.monto_total AS total,
        (SELECT COUNT(*) FROM tb_detalle_compra dc WHERE dc.id_compra = c.id_compra) AS itemCount
    FROM tb_compra c
    INNER JOIN tb_proveedor s ON s.id_proveedor = c.id_proveedor
    WHERE c.id_empresa = :companyId

      AND (:supplierId IS NULL OR c.id_proveedor = :supplierId)

      AND (
            :searchKey IS NULL
            OR LOWER(s.razon_social) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.referencia) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )

      AND (
            :fromDate IS NULL
            OR c.fecha_compra >= :fromDate
      )

      AND (
            :toDate IS NULL
            OR c.fecha_compra <= :toDate
      )

    ORDER BY c.fecha_creacion DESC
    """,

            countQuery = """
    SELECT COUNT(*)
    FROM tb_compra c
    INNER JOIN tb_proveedor s ON s.id_proveedor = c.id_proveedor
    WHERE c.id_empresa = :companyId

      AND (:supplierId IS NULL OR c.id_proveedor = :supplierId)

      AND (
            :searchKey IS NULL
            OR LOWER(s.razon_social) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.referencia) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )

      AND (
            :fromDate IS NULL
            OR c.fecha_compra >= :fromDate
      )

      AND (
            :toDate IS NULL
            OR c.fecha_compra <= :toDate
      )
    """,

            nativeQuery = true
    )
    Page<PurchaseDtoInter> findPurchasesByFilters(
            @Param("companyId") Long companyId,
            @Param("searchKey") String searchKey,
            @Param("supplierId") Long supplierId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

}
