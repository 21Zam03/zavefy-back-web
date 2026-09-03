package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_oportunidad")
public class OpportunityDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_oportunidad")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oportunidad")
    private OpportunityEntity opportunityEntity;

    @Column(name = "id_producto")
    private Long productId;

    @Column(name = "cantidad", precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "precio_en_momento")
    private BigDecimal priceAtMoment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpportunityEntity getOpportunityEntity() {
        return opportunityEntity;
    }

    public void setOpportunityEntity(OpportunityEntity opportunityEntity) {
        this.opportunityEntity = opportunityEntity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtMoment() {
        return priceAtMoment;
    }

    public void setPriceAtMoment(BigDecimal priceAtMoment) {
        this.priceAtMoment = priceAtMoment;
    }
}
