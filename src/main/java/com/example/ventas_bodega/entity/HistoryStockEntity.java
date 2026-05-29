package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_historial_stock")
public class HistoryStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial_stock")
    private Long historyStockId;

    @Column(name = "evento")
    private String event;

    @Column(name = "fecha_creacion")
    private LocalDateTime createDate;

    @Column(name = "stock_antes")
    private Long stockBefore;

    @Column(name = "stock_despues")
    private Long stockAfter;

    @Column(name = "stock_variacion")
    private Long stockVariation;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "venta_id")
    private Long saleId;

    @Column(name = "compra_id")
    private Long purchaseId;

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
    }

    public Long getHistoryStockId() {
        return historyStockId;
    }

    public void setHistoryStockId(Long historyStockId) {
        this.historyStockId = historyStockId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getStockBefore() {
        return stockBefore;
    }

    public void setStockBefore(Long stockBefore) {
        this.stockBefore = stockBefore;
    }

    public Long getStockAfter() {
        return stockAfter;
    }

    public void setStockAfter(Long stockAfter) {
        this.stockAfter = stockAfter;
    }

    public Long getStockVariation() {
        return stockVariation;
    }

    public void setStockVariation(Long stockVariation) {
        this.stockVariation = stockVariation;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
