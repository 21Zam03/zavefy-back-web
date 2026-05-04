package com.example.ventas_bodega.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductFoodDto {

    private String id;
    @JsonProperty("product_name")
    private String productName;
    private String brand;
    private String quantity;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("categories")
    private String categories;

    public ProductFoodDto(String id, String productName, String brand, String quantity, String imageUrl,  String categories) {
        this.id = id;
        this.productName = productName;
        this.brand = brand;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<String> getCategoriesList() {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        return Arrays.stream(categories.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ProductFoodDto{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", brand='" + brand + '\'' +
                ", quantity='" + quantity + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
}
