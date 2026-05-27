package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;
import com.example.ventas_bodega.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query(value = """
    SELECT 
        c.id_cliente AS clientId,
        c.nombres AS firstname,
        c.apellidos AS lastname,
        c.correo AS email,
        c.numero_documento AS documentNumber,
        c.tipo_documento AS documentType,
        c.numero_telefono AS phoneNumber,
        c.activo AS enabled,
        c.fecha_creacion AS createdDate,
        c.fecha_actualizacion AS updateDate
    FROM tb_cliente c
    WHERE c.id_empresa = :companyId

      AND (:active IS NULL OR c.activo = :active)

      AND (
            :searchKey IS NULL
            OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.correo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR c.numero_documento LIKE CONCAT('%', :searchKey, '%')
            OR c.numero_telefono LIKE CONCAT('%', :searchKey, '%')
      )

      AND (
            :documentType IS NULL
            OR c.tipo_documento = :documentType
      )

      AND (
            :fromDate IS NULL
            OR DATE(c.fecha_creacion) >= DATE(:fromDate)
      )

      AND (
            :toDate IS NULL
            OR DATE(c.fecha_creacion) <= DATE(:toDate)
      )

    ORDER BY c.fecha_actualizacion DESC
    """,

            countQuery = """
    SELECT COUNT(*)
    FROM tb_cliente c
    WHERE c.id_empresa = :companyId

      AND (:active IS NULL OR c.activo = :active)

      AND (
            :searchKey IS NULL
            OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(c.correo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR c.numero_documento LIKE CONCAT('%', :searchKey, '%')
            OR c.numero_telefono LIKE CONCAT('%', :searchKey, '%')
      )

      AND (
            :documentType IS NULL
            OR c.tipo_documento = :documentType
      )

      AND (
            :fromDate IS NULL
            OR DATE(c.fecha_creacion) >= DATE(:fromDate)
      )

      AND (
            :toDate IS NULL
            OR DATE(c.fecha_creacion) <= DATE(:toDate)
      )
    """,

            nativeQuery = true
    )
    Page<ClientDtoInter> findClientsByFilters(
            @Param("companyId") Long companyId,
            @Param("searchKey") String searchKey,
            @Param("active") Boolean active,
            @Param("documentType") String documentType,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

    List<ClientEntity> findByCompanyId(Long companyId);

}
