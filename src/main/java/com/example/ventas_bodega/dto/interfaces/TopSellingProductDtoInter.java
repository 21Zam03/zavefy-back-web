package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;

public interface TopSellingProductDtoInter {

    Long getProductId();
    String getName();
    String getBarcode();
    String getImageUrl();
    String getImageUrlThumb();
    BigDecimal getPrice();
    BigDecimal getStock();
    Long getTotalSold();

}
