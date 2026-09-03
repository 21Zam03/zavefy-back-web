package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.SupplierDtoInter;
import com.example.ventas_bodega.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

    Optional<SupplierEntity> findBySupplierIdAndCompanyId(Long supplierId, Long companyId);

    @Query(value = """
    SELECT
        s.id_proveedor AS supplierId,
        s.razon_social AS businessName,
        s.nombre_contacto AS contactName,
        s.correo AS email,
        s.activo AS enabled,
        s.numero_documento AS documentNumber,
        s.tipo_documento AS documentType,
        s.numero_telefono AS phoneNumber,
        s.direccion AS address,
        s.fecha_creacion AS createdDate,
        s.fecha_actualizacion AS updatedDate
    FROM tb_proveedor s
    WHERE s.id_empresa = :companyId

      AND (:active IS NULL OR s.activo = :active)

      AND (
            :searchKey IS NULL
            OR LOWER(s.razon_social) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(s.nombre_contacto) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(s.correo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR s.numero_documento LIKE CONCAT('%', :searchKey, '%')
            OR s.numero_telefono LIKE CONCAT('%', :searchKey, '%')
      )

      AND (
            :documentType IS NULL
            OR s.tipo_documento = :documentType
      )

      AND (
            :fromDate IS NULL
            OR DATE(s.fecha_creacion) >= DATE(:fromDate)
      )

      AND (
            :toDate IS NULL
            OR DATE(s.fecha_creacion) <= DATE(:toDate)
      )

    ORDER BY s.fecha_actualizacion DESC
    """,

            countQuery = """
    SELECT COUNT(*)
    FROM tb_proveedor s
    WHERE s.id_empresa = :companyId

      AND (:active IS NULL OR s.activo = :active)

      AND (
            :searchKey IS NULL
            OR LOWER(s.razon_social) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(s.nombre_contacto) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(s.correo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR s.numero_documento LIKE CONCAT('%', :searchKey, '%')
            OR s.numero_telefono LIKE CONCAT('%', :searchKey, '%')
      )

      AND (
            :documentType IS NULL
            OR s.tipo_documento = :documentType
      )

      AND (
            :fromDate IS NULL
            OR DATE(s.fecha_creacion) >= DATE(:fromDate)
      )

      AND (
            :toDate IS NULL
            OR DATE(s.fecha_creacion) <= DATE(:toDate)
      )
    """,

            nativeQuery = true
    )
    Page<SupplierDtoInter> findSuppliersByFilters(
            @Param("companyId") Long companyId,
            @Param("searchKey") String searchKey,
            @Param("active") Boolean active,
            @Param("documentType") String documentType,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("UPDATE SupplierEntity s SET s.enabled = false WHERE s.supplierId = :supplierId AND s.companyId = :companyId")
    int deactivateSupplier(@Param("supplierId") Long supplierId, @Param("companyId") Long companyId);

    @Modifying
    @Transactional
    @Query("UPDATE SupplierEntity s SET s.enabled = true WHERE s.supplierId = :supplierId AND s.companyId = :companyId")
    int activateSupplier(@Param("supplierId") Long supplierId, @Param("companyId") Long companyId);

}
