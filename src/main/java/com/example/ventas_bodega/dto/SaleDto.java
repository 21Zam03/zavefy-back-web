package com.example.ventas_bodega.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDto {

    private Integer ventaId;
    private String type;
    private String serial;
    private Integer number;
    private BigDecimal total;
    private BigDecimal subTotal;
    private BigDecimal subTotalFinal;
    private BigDecimal igv;
    private LocalDateTime createdDate;
    private String registerDate;
    private String moneyType;
    private String clientName;
    private String clientDocumentNumber;
    private String clientDocumentType;
    private String clientPhoneNumber;
    private String paymentMethod;
    private BigDecimal discount;
    private BigDecimal cardCommissionPercent;
    private BigDecimal cardCommissionAmount;
    private BigDecimal totalWithCommission;
    private Integer clientId;
    private Boolean splitPayment;
    private Boolean partialPayment;
    private BigDecimal amountPaidNow;
    private BigDecimal pendingBalance;
    private List<SalePaymentLineDto> paymentLines;
    private List<SaleDetailDto> saleDetails;

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<SaleDetailDto> getSaleDetails() {
        return saleDetails;
    }

    public void setSaleDetails(List<SaleDetailDto> saleDetails) {
        this.saleDetails = saleDetails;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientDocumentNumber() {
        return clientDocumentNumber;
    }

    public void setClientDocumentNumber(String clientDocumentNumber) {
        this.clientDocumentNumber = clientDocumentNumber;
    }

    public String getClientDocumentType() {
        return clientDocumentType;
    }

    public void setClientDocumentType(String clientDocumentType) {
        this.clientDocumentType = clientDocumentType;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public BigDecimal getSubTotalFinal() {
        return subTotalFinal;
    }

    public void setSubTotalFinal(BigDecimal subTotalFinal) {
        this.subTotalFinal = subTotalFinal;
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

    public BigDecimal getTotalWithCommission() {
        return totalWithCommission;
    }

    public void setTotalWithCommission(BigDecimal totalWithCommission) {
        this.totalWithCommission = totalWithCommission;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Boolean getSplitPayment() {
        return splitPayment;
    }

    public void setSplitPayment(Boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    public Boolean getPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(Boolean partialPayment) {
        this.partialPayment = partialPayment;
    }

    public BigDecimal getAmountPaidNow() {
        return amountPaidNow;
    }

    public void setAmountPaidNow(BigDecimal amountPaidNow) {
        this.amountPaidNow = amountPaidNow;
    }

    public BigDecimal getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(BigDecimal pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public List<SalePaymentLineDto> getPaymentLines() {
        return paymentLines;
    }

    public void setPaymentLines(List<SalePaymentLineDto> paymentLines) {
        this.paymentLines = paymentLines;
    }
}
