package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_compra")
public class PurchaseItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra")
    private Long purchaseItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra")
    private PurchaseEntity purchaseEntity;

    @Column(name = "id_producto")
    private Long productId;

    @Column(name = "cantidad", precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "costo_unitario")
    private BigDecimal cost;

    @Column(name = "costo_total")
    private BigDecimal totalCost;

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public PurchaseEntity getPurchaseEntity() {
        return purchaseEntity;
    }

    public void setPurchaseEntity(PurchaseEntity purchaseEntity) {
        this.purchaseEntity = purchaseEntity;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
