package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Column(name = "stock_actual", precision = 12, scale = 3)
    private BigDecimal currentStock;

    @Column(name = "tipo_ajuste")
    private String adjustmentType;

    @Column(name = "cantidad", precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "nuevo_stock", precision = 12, scale = 3)
    private BigDecimal newStock;

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

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getNewStock() {
        return newStock;
    }

    public void setNewStock(BigDecimal newStock) {
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
