package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PrintJobEntity;
import com.example.ventas_bodega.enums.PrintJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrintJobRepository extends JpaRepository<PrintJobEntity, Long> {

    List<PrintJobEntity> findByAgentIdAndStatus(
            Long agentId,
            PrintJobStatus status
    );

}
