package com.example.ventas_bodega.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SalePdfDto {

    private String comertialName;
    private String socialReason;
    private String address;
    private String phoneNumber;
    private String email;
    private String ruc;
    private String saleName;
    private String clientName;
    private String clientAddress;
    private String clientDocumentNumber;
    private String clientDocumentType;
    private String moneyType;
    private LocalDateTime createdDate;
    private BigDecimal discount;
    private BigDecimal igv;
    private BigDecimal total;
    private String notes;
    private String imageUrl;
    private List<SaleDetailPdfDto> saleDetailPdfDtoList;

    public String getComertialName() {
        return comertialName;
    }

    public void setComertialName(String comertialName) {
        this.comertialName = comertialName;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
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

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<SaleDetailPdfDto> getSaleDetailPdfDtoList() {
        return saleDetailPdfDtoList;
    }

    public void setSaleDetailPdfDtoList(List<SaleDetailPdfDto> saleDetailPdfDtoList) {
        this.saleDetailPdfDtoList = saleDetailPdfDtoList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
