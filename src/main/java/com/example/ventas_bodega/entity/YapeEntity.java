package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_yape")
public class YapeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_yape")
    private Integer yapeId;

    @Column(name = "alias")
    private String aliasName;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Column(name = "imagen_qr")
    private String imageQr;

    @Column(name = "por_defecto")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    public Integer getYapeId() {
        return yapeId;
    }

    public void setYapeId(Integer yapeId) {
        this.yapeId = yapeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageQr() {
        return imageQr;
    }

    public void setImageQr(String imageQr) {
        this.imageQr = imageQr;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
