package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.OpportunityEntity;
import com.example.ventas_bodega.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OpportunityRepository extends JpaRepository<OpportunityEntity, Long> {

    @Query("""
    SELECT o
    FROM OpportunityEntity o
    WHERE o.assignedTo.userId = :userId
      AND (:status IS NULL OR o.status = :status)
      AND (:priority IS NULL OR o.priority = :priority)
      AND (:source IS NULL OR o.source = :source)
      AND (:subject IS NULL OR o.subject = :subject)
      AND (:result IS NULL OR o.result = :result)
      AND (:fromDate IS NULL OR o.createdDate >= :fromDate)
      AND (:toDate IS NULL OR o.createdDate <= :toDate)
""")
    Page<OpportunityEntity> findOpportunities(
            @Param("userId") Integer userId,
            @Param("status") OpportunityStatusEnum status,
            @Param("priority") PriorityEnum priority,
            @Param("source") SourceEnum source,
            @Param("subject") SubjectEnum subject,
            @Param("result") ResultEnum result,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);

    @Query(value = """
    SELECT numeracion
    FROM tb_oportunidad
    WHERE id_usuario_asignado = :userId
    ORDER BY id_oportunidad DESC
    LIMIT 1
    """, nativeQuery = true)
    Integer findLastOpportunityNumber(@Param("userId") Integer userId);
}
