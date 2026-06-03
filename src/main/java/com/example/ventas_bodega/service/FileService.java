package com.example.ventas_bodega.service;

import java.io.ByteArrayInputStream;

public interface FileService {

    public ByteArrayInputStream getPdfTicket(Long id) throws Exception;
    public String getTicketToPrint(Long id) throws Exception;

}
