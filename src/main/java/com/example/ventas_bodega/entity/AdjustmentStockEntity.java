package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ajuste_stock")
public class AdjustmentStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ajuste_stock")
    private Long adjustmentStockId;

    @Column(name = "id_producto")
    private Long productId;

    @Column(name = "stock_actual")
    private Integer currentStock;

    @Column(name = "tipo_ajuste")
    private String adjustmentType;

    @Column(name = "cantidad")
    private Integer quantity;

    @Column(name = "nuevo_stock")
    private Integer newStock;

    @Column(name = "motivo")
    private String reason;

    @Column(name = "observacion")
    private String observation;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdDate;

    @Column(name = "creado_por")
    private Long createdBy;


    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }

    public Long getAdjustmentStockId() {
        return adjustmentStockId;
    }

    public void setAdjustmentStockId(Long adjustmentStockId) {
        this.adjustmentStockId = adjustmentStockId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getNewStock() {
        return newStock;
    }

    public void setNewStock(Integer newStock) {
        this.newStock = newStock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
