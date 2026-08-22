package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.FeatureTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "tb_plan_caracteristica",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_plan_feature",
                        columnNames = {"plan_id", "feature"}
                )
        }
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanFeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_caracteristica")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "plan_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_plan_features_plan")
    )
    private PlanEntity plan;

    @Column(nullable = false, length = 100)
    private String feature;

    @Column(name = "feature_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FeatureTypeEnum featureType;

    @Column(nullable = false, length = 100)
    private String value;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
