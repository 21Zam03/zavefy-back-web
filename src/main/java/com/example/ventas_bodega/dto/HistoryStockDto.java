package com.example.ventas_bodega.dto;

import java.time.LocalDateTime;

public class HistoryStockDto {

    private Long historyStockId;
    private String event;
    private LocalDateTime createdDate;
    private Long stockBefore;
    private Long stockAfter;
    private Long stockVariation;
    private Long companyId;
    private Long createdBy;
    private String createdByName;
    private String productName;
    private Integer saleId;
    private String saleName;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}
