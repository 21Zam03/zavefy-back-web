package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.AgentEntity;
import com.example.ventas_bodega.enums.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<AgentEntity, Long> {

    Optional<AgentEntity> findFirstByCompanyIdAndStatus(
            Long companyId,
            AgentStatus status
    );

}
