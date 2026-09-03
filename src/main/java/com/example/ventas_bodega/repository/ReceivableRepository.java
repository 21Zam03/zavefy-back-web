package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.ReceivableDtoInter;
import com.example.ventas_bodega.entity.ReceivableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReceivableRepository extends JpaRepository<ReceivableEntity, Long> {

    Optional<ReceivableEntity> findByReceivableIdAndCompanyId(Long receivableId, Long companyId);

    @Query(value = """
    SELECT
        r.id_cuenta_por_cobrar AS receivableId,
        r.id_cliente AS clientId,
        c.nombre_completo AS clientName,
        r.id_venta AS saleId,
        r.monto_original AS originalAmount,
        r.saldo AS balance,
        r.concepto AS concept,
        r.fecha_limite AS dueDate,
        r.estado AS status,
        r.fecha_creacion AS createdDate
    FROM tb_cuenta_por_cobrar r
    INNER JOIN tb_cliente c ON c.id_cliente = r.id_cliente
    WHERE r.empresa_id = :companyId

      AND (:status IS NULL OR r.estado = :status)

      AND (:clientId IS NULL OR r.id_cliente = :clientId)

      AND (
            :searchKey IS NULL
            OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(r.concepto) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )

    ORDER BY r.fecha_creacion DESC
    """,

            countQuery = """
    SELECT COUNT(*)
    FROM tb_cuenta_por_cobrar r
    INNER JOIN tb_cliente c ON c.id_cliente = r.id_cliente
    WHERE r.empresa_id = :companyId

      AND (:status IS NULL OR r.estado = :status)

      AND (:clientId IS NULL OR r.id_cliente = :clientId)

      AND (
            :searchKey IS NULL
            OR LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(r.concepto) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )
    """,

            nativeQuery = true
    )
    Page<ReceivableDtoInter> findReceivablesByFilters(
            @Param("companyId") Long companyId,
            @Param("searchKey") String searchKey,
            @Param("status") String status,
            @Param("clientId") Integer clientId,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(r.balance), 0) FROM ReceivableEntity r WHERE r.client.clientId = :clientId AND r.companyId = :companyId AND r.status = 'PENDIENTE'")
    BigDecimal sumPendingBalanceByClient(@Param("clientId") Integer clientId, @Param("companyId") Long companyId);

    // Usadas por el cron de notificaciones (NotificationServiceImpl): escanean todas las empresas de una vez.
    @Query(value = """
    SELECT r.empresa_id, r.id_cuenta_por_cobrar, c.nombre_completo, r.saldo
    FROM tb_cuenta_por_cobrar r
    INNER JOIN tb_cliente c ON c.id_cliente = r.id_cliente
    WHERE r.estado = 'PENDIENTE'
      AND r.fecha_limite IS NOT NULL
      AND r.fecha_limite >= CURDATE()
      AND r.fecha_limite <= DATE_ADD(CURDATE(), INTERVAL :daysAhead DAY)
    """, nativeQuery = true)
    List<Object[]> findReceivablesDueSoon(@Param("daysAhead") int daysAhead);

    @Query(value = """
    SELECT r.empresa_id, r.id_cuenta_por_cobrar, c.nombre_completo, r.saldo
    FROM tb_cuenta_por_cobrar r
    INNER JOIN tb_cliente c ON c.id_cliente = r.id_cliente
    WHERE r.estado = 'PENDIENTE'
      AND r.fecha_limite IS NOT NULL
      AND r.fecha_limite < CURDATE()
    """, nativeQuery = true)
    List<Object[]> findReceivablesOverdue();

}
