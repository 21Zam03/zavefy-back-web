package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByFingerprint(String fingerprint);
    Optional<PaymentEntity> findByFingerprint(String fingerprint);

}
