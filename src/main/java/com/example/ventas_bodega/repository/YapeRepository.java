package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.YapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface YapeRepository extends JpaRepository<YapeEntity, Long> {

    @Query("SELECT y FROM YapeEntity y WHERE y.company.id = :companyId")
    List<YapeEntity> findByCompanyId(Long companyId);

    Optional<YapeEntity> findByYapeIdAndCompanyCompanyId(
            Integer yapeId,
            Long companyId
    );

    Optional<YapeEntity> findFirstByCompanyCompanyIdOrderByYapeIdAsc(
            Long companyId
    );

}
