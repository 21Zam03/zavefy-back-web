package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.ReceivablePaymentDtoInter;
import com.example.ventas_bodega.entity.ReceivablePaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceivablePaymentRepository extends JpaRepository<ReceivablePaymentEntity, Long> {

    Page<ReceivablePaymentEntity> findByReceivable_ReceivableIdOrderByPaymentDateDesc(Long receivableId, Pageable pageable);

    @Query(
            value = """
    SELECT
        a.id_abono AS paymentId,
        a.id_cuenta_por_cobrar AS receivableId,
        a.monto AS amount,
        a.metodo_pago AS paymentMethod,
        a.fecha_pago AS paymentDate,
        a.creado_por AS createdBy,
        c.nombre_completo AS clientName,
        CONCAT(u.nombres, ' ', u.apellidos) AS registeredByName
    FROM tb_abono a
    JOIN tb_cuenta_por_cobrar cxc ON cxc.id_cuenta_por_cobrar = a.id_cuenta_por_cobrar
    JOIN tb_cliente c ON c.id_cliente = cxc.id_cliente
    LEFT JOIN tb_usuario u ON u.id_usuario = a.creado_por
    WHERE cxc.empresa_id = :companyId
      AND (:clientId IS NULL OR cxc.id_cliente = :clientId)
      AND (:paymentMethod IS NULL OR a.metodo_pago = :paymentMethod)
      AND (:searchKey IS NULL OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :searchKey, '%')))
      AND (:fromDate IS NULL OR DATE(a.fecha_pago) >= DATE(:fromDate))
      AND (:toDate IS NULL OR DATE(a.fecha_pago) <= DATE(:toDate))
    ORDER BY a.fecha_pago DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM tb_abono a
    JOIN tb_cuenta_por_cobrar cxc ON cxc.id_cuenta_por_cobrar = a.id_cuenta_por_cobrar
    JOIN tb_cliente c ON c.id_cliente = cxc.id_cliente
    WHERE cxc.empresa_id = :companyId
      AND (:clientId IS NULL OR cxc.id_cliente = :clientId)
      AND (:paymentMethod IS NULL OR a.metodo_pago = :paymentMethod)
      AND (:searchKey IS NULL OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :searchKey, '%')))
      AND (:fromDate IS NULL OR DATE(a.fecha_pago) >= DATE(:fromDate))
      AND (:toDate IS NULL OR DATE(a.fecha_pago) <= DATE(:toDate))
    """,
            nativeQuery = true
    )
    Page<ReceivablePaymentDtoInter> findByCompanyWithFilters(
            @Param("companyId") Long companyId,
            @Param("clientId") Integer clientId,
            @Param("paymentMethod") String paymentMethod,
            @Param("searchKey") String searchKey,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable
    );

}
