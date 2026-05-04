package com.example.ventas_bodega.dto;

public class ProductAlertDto {

    private String productName;
    private Long stock;
    private String stockState;

    public ProductAlertDto() {}

    public ProductAlertDto(String productName, Long stock, String stockState) {
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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }
}
