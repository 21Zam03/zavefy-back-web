package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ProductFoodDto;
import com.example.ventas_bodega.response.OpenFoodFactsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FoodRestTemplateImpl implements FoodRestTemplate {

    private final String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private final RestTemplate restTemplate;

    @Autowired
    public FoodRestTemplateImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductFoodDto getProductByBarcode(String barcode) {
        String url = BASE_URL + barcode + ".json";
        OpenFoodFactsResponse response = restTemplate.getForObject(url, OpenFoodFactsResponse.class);
        if (response != null && response.getStatus() == 1 && response.getProduct() != null) {
            ProductFoodDto p = response.getProduct();
            System.out.println("PRODUCTO: "+p.toString());
            return p;
        }
        return null;
    }

}
