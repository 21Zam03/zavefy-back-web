package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;

public interface SaleDetailDtoInter {

    Long getId();
    Integer getQuantity();
    BigDecimal getUnitePrice();
    BigDecimal getTotal();
    Long getProductId();
    String getProductName();
    String getNotes();
    String getMeasurementUnit();

}
