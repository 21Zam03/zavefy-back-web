package com.example.ventas_bodega.request;

import java.math.BigDecimal;

public class CreateReceivablePaymentRequest {

    private BigDecimal amount;
    private String paymentMethod;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
