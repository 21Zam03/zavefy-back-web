package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.BillingPeriodEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_planes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer id;

    @Column(name="nombre", nullable = false, length = 100)
    private String name;

    @Column(name="descripcion", length = 500)
    private String description;

    @Column(name="precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name="tipo_moneda", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodo_facturacion", nullable = false, length = 20)
    private BillingPeriodEnum billingPeriod;

    @Column(name = "es_activo", nullable = false)
    private Boolean active = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanFeatureEntity> features = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}