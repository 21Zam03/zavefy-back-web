package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.AgentEntity;
import com.example.ventas_bodega.enums.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<AgentEntity, Long> {

    Optional<AgentEntity> findFirstByCompanyIdAndStatus(
            Long companyId,
            AgentStatus status
    );

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_agente
        SET ultimo_visto = :now
        WHERE id = :agentId
        """,
            nativeQuery = true
    )
    int updateLastSeen(@Param("agentId") Long agentId, @Param("now") LocalDateTime now);

    @Query(
            value = """
        SELECT ultimo_visto
        FROM tb_agente
        WHERE id = :agentId
        """,
            nativeQuery = true
    )
    LocalDateTime findLastSeenById(
            @Param("agentId") Long agentId
    );

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_agente
        SET impresora_predeterminada = :printerName
        WHERE id = :agentId
        """,
            nativeQuery = true
    )
    int updateDefaultPrinter(
            @Param("agentId") Long agentId,
            @Param("printerName") String printerName
    );

}
