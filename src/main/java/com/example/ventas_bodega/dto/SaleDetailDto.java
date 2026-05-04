package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class SaleDetailDto {

    private Long id;
    private Long saleId;
    private Integer quantity;
    private BigDecimal unitePrice;
    private BigDecimal total;
    private Long productId;

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
}
