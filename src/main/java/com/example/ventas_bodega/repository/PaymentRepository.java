package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByFingerprint(String fingerprint);
    Optional<PaymentEntity> findByFingerprint(String fingerprint);

    @Query("""
        SELECT p FROM PaymentEntity p
        WHERE p.companyId = :companyId
        AND (:source IS NULL OR p.source = :source)
        AND (:status IS NULL OR p.status = :status)
        ORDER BY p.receivedAt DESC
        """)
    Page<PaymentEntity> findByCompanyIdAndFilters(
            @Param("companyId") Long companyId,
            @Param("source") String source,
            @Param("status") PaymentStatus status,
            Pageable pageable
    );

}
