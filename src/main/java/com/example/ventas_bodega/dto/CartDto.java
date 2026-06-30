package com.example.ventas_bodega.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CartDto {

    private Long id;
    private Long companyId;
    private String customerPhoneNumber;
    private String customerName;
    private String subject;
    private String status;
    private LocalDateTime createdAt;
    private List<CartDetailDto> cartDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartDetailDto> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetailDto> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
