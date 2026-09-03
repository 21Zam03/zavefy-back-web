package com.example.ventas_bodega.dto.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReceivablePaymentDtoInter {

    Long getPaymentId();
    Long getReceivableId();
    BigDecimal getAmount();
    String getPaymentMethod();
    LocalDateTime getPaymentDate();
    Long getCreatedBy();
    String getClientName();
    String getRegisteredByName();

}
