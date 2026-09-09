package com.example.ventas_bodega.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDataDto {

    private BigDecimal total;
    private Long saleCount;
    private BigDecimal productCount;
    private BigDecimal averageTicket;
    private BigDecimal totalCost;
    private BigDecimal totalMargin;
    private BigDecimal marginPercent;
    private BigDecimal totalPurchases;
    private List<SalexDay> salexDays;
    private List<TopProductDto> topProducts;
    private List<ProductAlertDto> productAlerts;

    public DashboardDataDto() {}

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public BigDecimal getProductCount() {
        return productCount;
    }

    public void setProductCount(BigDecimal productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getAverageTicket() {
        return averageTicket;
    }

    public void setAverageTicket(BigDecimal averageTicket) {
        this.averageTicket = averageTicket;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalMargin() {
        return totalMargin;
    }

    public void setTotalMargin(BigDecimal totalMargin) {
        this.totalMargin = totalMargin;
    }

    public BigDecimal getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(BigDecimal marginPercent) {
        this.marginPercent = marginPercent;
    }

    public BigDecimal getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(BigDecimal totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public List<SalexDay> getSalexDays() {
        return salexDays;
    }

    public void setSalexDays(List<SalexDay> salexDays) {
        this.salexDays = salexDays;
    }

    public List<TopProductDto> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<TopProductDto> topProducts) {
        this.topProducts = topProducts;
    }

    public List<ProductAlertDto> getProductAlerts() {
        return productAlerts;
    }

    public void setProductAlerts(List<ProductAlertDto> productAlerts) {
        this.productAlerts = productAlerts;
    }
}
