package com.example.ventas_bodega.response;

import com.example.ventas_bodega.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private String message;
    private ProductDto product;

}
