package com.example.ventas_bodega.response;

import com.example.ventas_bodega.dto.ProductFoodDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsResponse {

    private String code;
    private ProductFoodDto product;
    private int status;

    public OpenFoodFactsResponse(String code, int status, ProductFoodDto product) {
        this.code = code;
        this.status = status;
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProductFoodDto getProduct() {
        return product;
    }

    public void setProduct(ProductFoodDto product) {
        this.product = product;
    }
}
