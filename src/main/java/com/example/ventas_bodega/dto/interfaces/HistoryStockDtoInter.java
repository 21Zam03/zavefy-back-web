package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface HistoryStockDtoInter {

    Long getHistoryStockId();

    String getEvent();

    LocalDateTime getCreateDate();

    BigDecimal getStockBefore();

    BigDecimal getStockAfter();

    BigDecimal getStockVariation();

    Long getCompanyId();

    Long getCreatedBy();

    String getCreatedByName();

    String getProductName();

    Integer getSaleId();

    String getSaleName();

}
