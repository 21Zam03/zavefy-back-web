package com.example.ventas_bodega.response;

import com.example.ventas_bodega.dto.ProductDto;

import java.util.List;

public class SignInResponse {

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String companyName;
    private String role;
    private boolean hasStock;
    private boolean hasBarcode;
    private String message;
    private String token;
    private Integer status;
    private List<ProductDto> products;

    public SignInResponse() {
    }

    public SignInResponse(String email, String companyName, String role, String message, String token, Integer status, String username) {
        this.email = email;
        this.companyName = companyName;
        this.role = role;
        this.message = message;
        this.token = token;
        this.status = status;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public boolean isHasBarcode() {
        return hasBarcode;
    }

    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
