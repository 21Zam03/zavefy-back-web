package com.example.ventas_bodega.dto;

import org.springframework.web.multipart.MultipartFile;

public class CompanyDto {

    private Long companyId;
    private String ruc;
    private String socialReason;
    private String comertialName;
    private String address;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private MultipartFile file;
    private String filePath;
    private boolean hasStock;
    private boolean hasAutomaticSaved;
    private boolean hasBarcode;
    private boolean isTest;
    private boolean hasPrinter;

    public CompanyDto() {
    }

    public CompanyDto(Long companyId, String ruc, String socialReason, String comertialName, String address, String email, String phoneNumber, String imageUrl) {
        this.companyId = companyId;
        this.ruc = ruc;
        this.socialReason = socialReason;
        this.comertialName = comertialName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getComertialName() {
        return comertialName;
    }

    public void setComertialName(String comertialName) {
        this.comertialName = comertialName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public boolean isHasPrinter() {
        return hasPrinter;
    }

    public void setHasPrinter(boolean hasPrinter) {
        this.hasPrinter = hasPrinter;
    }

    public boolean isHasAutomaticSaved() {
        return hasAutomaticSaved;
    }

    public void setHasAutomaticSaved(boolean hasAutomaticSaved) {
        this.hasAutomaticSaved = hasAutomaticSaved;
    }
}
