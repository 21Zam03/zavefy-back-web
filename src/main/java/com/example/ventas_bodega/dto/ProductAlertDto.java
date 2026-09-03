package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class ProductAlertDto {

    private String productName;
    private BigDecimal stock;
    private String stockState;

    public ProductAlertDto() {}

    public ProductAlertDto(String productName, BigDecimal stock, String stockState) {
        this.productName = productName;
        this.stock = stock;
        this.stockState = stockState;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }
}
