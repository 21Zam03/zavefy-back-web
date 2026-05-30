package com.example.ventas_bodega.dto.interfaces;

import java.time.LocalDateTime;

public interface ClientDtoInter {

    Integer getClientId();

    String getFirstname();

    String getLastname();

    String getEmail();

    String getDocumentNumber();

    String getDocumentType();

    String getPhoneNumber();

    Boolean getEnabled();

    LocalDateTime getCreatedDate();
    LocalDateTime getUpdatedDate();
    String getAddress();
    String getFullName();

}
