package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ProductFoodDto;

public interface FoodRestTemplate {

    public ProductFoodDto getProductByBarcode(String barcode);

}
