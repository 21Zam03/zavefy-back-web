package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class TopProductDto {

    private String productName;
    private Integer totalSales;
    private BigDecimal totalIncome;

    public TopProductDto(String productName, Integer totalSales, BigDecimal totalIncome) {
        this.productName = productName;
        this.totalSales = totalSales;
        this.totalIncome = totalIncome;
    }

    public TopProductDto() {}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
}
