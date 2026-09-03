package com.example.ventas_bodega.dto.interfaces;

import java.time.LocalDateTime;

public interface SupplierDtoInter {

    Long getSupplierId();

    String getBusinessName();

    String getContactName();

    String getEmail();

    Boolean getEnabled();

    String getDocumentNumber();

    String getDocumentType();

    String getPhoneNumber();

    String getAddress();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdatedDate();

}
