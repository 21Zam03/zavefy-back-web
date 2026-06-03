package com.example.ventas_bodega.dto.interfaces;

import java.time.LocalDateTime;

public interface HistoryStockDtoInter {

    Long getHistoryStockId();

    String getEvent();

    LocalDateTime getCreateDate();

    Long getStockBefore();

    Long getStockAfter();

    Long getStockVariation();

    Long getCompanyId();

    Long getCreatedBy();

    String getCreatedByName();

    String getProductName();

    Integer getSaleId();

    String getSaleName();

}
