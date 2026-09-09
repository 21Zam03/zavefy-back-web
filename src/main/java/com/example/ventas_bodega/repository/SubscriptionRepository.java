package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    // Mantenimiento > Empresas (SUPER_ADMIN): trae las suscripciones de las empresas de la
    // página actual para mostrar plan/estado en el listado (se resuelve la más reciente por
    // empresa en memoria, evitando una query por empresa).
    List<SubscriptionEntity> findByCompany_CompanyIdIn(List<Long> companyIds);

}
