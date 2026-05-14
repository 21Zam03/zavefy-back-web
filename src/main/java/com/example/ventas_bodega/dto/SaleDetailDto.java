package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class SaleDetailDto {

    private Long id;
    private Long saleId;
    private Integer quantity;
    private BigDecimal unitePrice;
    private BigDecimal total;
    private Long productId;
    private String name;
    private String measurementUnit;
    private boolean hasAutomaticSaved;
    private String notes;

    public SaleDetailDto() {}

    public SaleDetailDto(Long id, Long saleId, Integer quantity, BigDecimal unitePrice, BigDecimal total) {
        this.id = id;
        this.saleId = saleId;
        this.quantity = quantity;
        this.unitePrice = unitePrice;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(BigDecimal unitePrice) {
        this.unitePrice = unitePrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public boolean isHasAutomaticSaved() {
        return hasAutomaticSaved;
    }

    public void setHasAutomaticSaved(boolean hasAutomaticSaved) {
        this.hasAutomaticSaved = hasAutomaticSaved;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
