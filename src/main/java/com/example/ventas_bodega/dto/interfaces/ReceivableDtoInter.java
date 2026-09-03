package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReceivableDtoInter {

    Long getReceivableId();

    Integer getClientId();

    String getClientName();

    Integer getSaleId();

    BigDecimal getOriginalAmount();

    BigDecimal getBalance();

    String getConcept();

    LocalDate getDueDate();

    String getStatus();

    LocalDateTime getCreatedDate();

}
