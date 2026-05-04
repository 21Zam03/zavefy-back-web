package com.example.ventas_bodega.dto;

public class YapeDto {

    private Integer yapeId;
    private String phoneNumber;
    private String imageQr;
    private boolean isDefault;
    private Long companyId;

    public YapeDto() {}

    public YapeDto(Integer yapeId, String phoneNumber, String imageQr, boolean isDefault, Long companyId) {
        this.yapeId = yapeId;
        this.phoneNumber = phoneNumber;
        this.imageQr = imageQr;
        this.isDefault = isDefault;
        this.companyId = companyId;
    }

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
