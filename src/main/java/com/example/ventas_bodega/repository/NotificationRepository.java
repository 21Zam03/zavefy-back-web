package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    boolean existsByCompanyIdAndTypeAndReferenceIdAndReadFalse(Long companyId, String type, Long referenceId);

    Optional<NotificationEntity> findByNotificationIdAndCompanyId(Long notificationId, Long companyId);

    Page<NotificationEntity> findByCompanyIdOrderByCreatedAtDesc(Long companyId, Pageable pageable);

    Page<NotificationEntity> findByCompanyIdAndReadOrderByCreatedAtDesc(Long companyId, boolean read, Pageable pageable);

    long countByCompanyIdAndReadFalse(Long companyId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.companyId = :companyId AND n.read = false")
    void markAllAsReadByCompanyId(@Param("companyId") Long companyId);

}
