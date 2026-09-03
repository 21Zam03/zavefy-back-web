package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class SalePaymentLineDto {

    private String method;
    private BigDecimal amount;
    private BigDecimal cardCommissionPercent;
    private BigDecimal cardCommissionAmount;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCardCommissionPercent() {
        return cardCommissionPercent;
    }

    public void setCardCommissionPercent(BigDecimal cardCommissionPercent) {
        this.cardCommissionPercent = cardCommissionPercent;
    }

    public BigDecimal getCardCommissionAmount() {
        return cardCommissionAmount;
    }

    public void setCardCommissionAmount(BigDecimal cardCommissionAmount) {
        this.cardCommissionAmount = cardCommissionAmount;
    }
}
