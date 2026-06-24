package com.example.ventas_bodega.service;

import java.io.ByteArrayInputStream;

public interface FileService {

    public ByteArrayInputStream getPdfTicket(Long id) throws Exception;
    public String getTicketToPrint(Long id) throws Exception;
    public ByteArrayInputStream getExcelSaleReport(String ruc, String type, String serial, Integer number, String fromDate, String toDate) throws Exception;
    public ByteArrayInputStream getPdfSaleReport(String ruc, String type, String serial, Integer number, String fromDate, String toDate) throws Exception;
    public ByteArrayInputStream getExcelProductReport(Long companyId, String barcode, String name, String stockStatus, Boolean active, Long categoryId) throws Exception;
    public ByteArrayInputStream getPdfProductReport(Long companyId, String barcode, String name, String stockStatus, Boolean active, Long categoryId) throws Exception;

}
