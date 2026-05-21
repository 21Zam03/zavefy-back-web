package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_empresa")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long companyId;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "razon_social")
    private String socialReason;

    @Column(name = "nombre_comercial")
    private String comertialName;

    @Column(name = "direccion")
    private String address;

    @Column(name = "correo")
    private String email;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Column(name = "url_imagen")
    private String imageUrl;

    @Column(name = "tiene_stock")
    private boolean hasStock;

    @Column(name = "ruta")
    private String filePath;

    @Column(name = "tiene_codigo_barras")
    private boolean hasBarcode;

    @Column(name = "tiene_guardado_automatico")
    private boolean hasAutomaticSaved;

    @Column(name = "es_prueba")
    private boolean isTest;

    public CompanyEntity() {}

    public CompanyEntity(Long companyId, String comertialName, String ruc) {
        this.companyId = companyId;
        this.comertialName = comertialName;
        this.ruc = ruc;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getComertialName() {
        return comertialName;
    }

    public void setComertialName(String comertialName) {
        this.comertialName = comertialName;
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

    public boolean isHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isHasBarcode() {
        return hasBarcode;
    }

    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    public boolean isHasAutomaticSaved() {
        return hasAutomaticSaved;
    }

    public void setHasAutomaticSaved(boolean hasAutomaticSaved) {
        this.hasAutomaticSaved = hasAutomaticSaved;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "companyId=" + companyId +
                ", ruc='" + ruc + '\'' +
                ", socialReason='" + socialReason + '\'' +
                ", comertialName='" + comertialName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", hasStock=" + hasStock +
                ", filePath='" + filePath + '\'' +
                ", hasBarcode=" + hasBarcode +
                ", hasAutomaticSaved=" + hasAutomaticSaved +
                '}';
    }
}
