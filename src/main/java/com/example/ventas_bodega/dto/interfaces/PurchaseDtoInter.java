package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;

public interface PurchaseDtoInter {

    Long getPurchaseId();

    Long getSupplierId();

    String getSupplierName();

    String getPurchaseDate();

    String getReference();

    BigDecimal getTotal();

    Long getItemCount();

}
