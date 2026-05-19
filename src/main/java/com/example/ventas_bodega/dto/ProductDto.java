package com.example.ventas_bodega.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class ProductDto {

    private Long id;
    private String barcode;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal unitPrice;
    private List<String> categories;
    private boolean active;
    private String imageUrl;
    private Integer stock;
    private MultipartFile file;
    private String filePath;
    private String createdDate;
    private String updatedDate;
    private String measurementUnit;
    private boolean automaticSaved;
    private String notes;

    public ProductDto() {
    }

    public ProductDto(Long id, String barcode, String name, String description, BigDecimal price, List<String> categories, boolean active, String imageUrl, String filePaht) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.active = active;
        this.imageUrl = imageUrl;
        this.filePath = filePaht;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public boolean isAutomaticSaved() {
        return automaticSaved;
    }

    public void setAutomaticSaved(boolean automaticSaved) {
        this.automaticSaved = automaticSaved;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", unitPrice=" + unitPrice +
                ", categories=" + categories +
                ", active=" + active +
                ", imageUrl='" + imageUrl + '\'' +
                ", stock=" + stock +
                ", file=" + file +
                ", filePath='" + filePath + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", measurementUnit='" + measurementUnit + '\'' +
                ", automaticSaved=" + automaticSaved +
                ", notes='" + notes + '\'' +
                '}';
    }
}
