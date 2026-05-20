package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.SaleDetailPdfDto;
import com.example.ventas_bodega.dto.SalePdfDto;
import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.SaleEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.SaleDetailRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.service.FileService;
import com.example.ventas_bodega.util.DateUtil;
import com.example.ventas_bodega.util.MoneyUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private final SaleRepository saleRepository;
    private final CompanyRepository companyRepository;
    private final SaleDetailRepository saleDetailRepository;

    @Autowired
    public FileServiceImpl(SaleRepository saleRepository, CompanyRepository companyRepository, SaleDetailRepository saleDetailRepository) {
        this.saleRepository = saleRepository;
        this.companyRepository = companyRepository;
        this.saleDetailRepository = saleDetailRepository;
    }

    @Override
    @Transactional
    public ByteArrayInputStream getPdfTicket(Long id) throws Exception {
        try {
            SaleEntity saleEntity = saleRepository.findById(id).orElseThrow(() -> {
                return new NotFoundException("SALE NOT FOUND");
            });
            System.out.println("SALE: " + saleEntity.toString());
            CompanyEntity company = companyRepository.findByRuc(saleEntity.getUser().getCompany().getRuc());

            List<SaleDetailDtoInter> productList = saleDetailRepository.findDetailsBySaleId(id);
            List<SaleDetailPdfDto> saleDetailPdfDtoList = new ArrayList<>();
            for (SaleDetailDtoInter saleDetailDtoInter : productList) {
                SaleDetailPdfDto saleDetailPdfDto = new SaleDetailPdfDto();
                saleDetailPdfDto.setBarcode(productList.get(0).getBarcode());
                saleDetailPdfDto.setProductName(saleDetailDtoInter.getProductName());
                saleDetailPdfDto.setUnitPrice(String.valueOf(saleDetailDtoInter.getUnitePrice()));
                saleDetailPdfDto.setTotalPrice(String.valueOf(saleDetailDtoInter.getTotal()));
                saleDetailPdfDto.setMeasurementUnit(String.valueOf(saleDetailDtoInter.getMeasurementUnit()));
                saleDetailPdfDto.setQuantity(String.valueOf(saleDetailDtoInter.getQuantity()));
                saleDetailPdfDtoList.add(saleDetailPdfDto);
            }
            SalePdfDto salePdfDto = new SalePdfDto();
            salePdfDto.setComertialName(company.getComertialName().toUpperCase());
            salePdfDto.setSocialReason(company.getSocialReason());
            salePdfDto.setAddress(company.getAddress());
            salePdfDto.setPhoneNumber(company.getPhoneNumber());
            salePdfDto.setEmail(company.getEmail());
            salePdfDto.setRuc(company.getRuc());
            salePdfDto.setClientName(saleEntity.getClientName());
            salePdfDto.setClientAddress(saleEntity.getClientAddress());
            salePdfDto.setClientDocumentNumber(saleEntity.getClientDocumentNumber());
            salePdfDto.setClientDocumentType(saleEntity.getClientDocumentType());
            salePdfDto.setMoneyType(saleEntity.getMoneyType());
            salePdfDto.setCreatedDate(saleEntity.getCreatedDate());
            salePdfDto.setDiscount(saleEntity.getDiscount());
            salePdfDto.setIgv(saleEntity.getIgv());
            salePdfDto.setTotal(saleEntity.getTotal());
            salePdfDto.setSubTotal(saleEntity.getSubTotal());
            salePdfDto.setNotes(saleEntity.getNotes());
            salePdfDto.setSaleDetailPdfDtoList(saleDetailPdfDtoList);
            salePdfDto.setImageUrl(company.getImageUrl());
            salePdfDto.setType(saleEntity.getType());
            salePdfDto.setIdentifier(saleEntity.getIdentifier());
            salePdfDto.setSeller(saleEntity.getUser().getFirstname()+" "+saleEntity.getUser().getLastname());
            return new ByteArrayInputStream(getByteFromPdfTicket(salePdfDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public byte[] getByteFromPdfTicket(SalePdfDto salePdfDto) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("nombreComercial", salePdfDto.getComertialName().toUpperCase());
        params.put("razonSocial", salePdfDto.getSocialReason().toUpperCase());
        params.put("direccion", salePdfDto.getAddress().toUpperCase());
        params.put("telefono", salePdfDto.getPhoneNumber());
        params.put("email", salePdfDto.getEmail());
        params.put("ruc", salePdfDto.getRuc());

        String saleName = "";
        if(salePdfDto.getType().equals("03")) {
            saleName = "BOLETA DE VENTA ELECTRONICA";
        } else {
            saleName = "FACTURA DE VENTA ELECTRÓNICA";
        }
        params.put("nombreComprobante", saleName);
        params.put("serieCorrelativo" , getSerieAndNumber(salePdfDto.getIdentifier()));
        params.put("nombreCliente", salePdfDto.getClientName());
        params.put("direccionCliente", salePdfDto.getClientAddress());
        params.put("observaciones", salePdfDto.getNotes());

        String tipoDocumento = "";
        switch (salePdfDto.getClientDocumentType()) {
            case "0":
                tipoDocumento = "DOC.TRIB.NO.DOM.SIN.RUC";
                break;
            case "1":
                tipoDocumento = "DNI";
                break;
            case "4":
                tipoDocumento = "CE";
                break;
            case "6":
                tipoDocumento = "RUC";
                break;
            case "7":
                tipoDocumento = "PASAPORTE";
                break;
            case "A":
                tipoDocumento = "CE DIPLOMÁTICA DE IDENTIDAD";
                break;
            default:
                tipoDocumento = "Tipo de documento desconocido";
        }

        params.put("tipoDocCliente", tipoDocumento);
        params.put("numDocCliente", salePdfDto.getClientDocumentNumber());
        params.put("moneda", salePdfDto.getMoneyType());
        params.put("fecha", DateUtil.formatToYearMonthDay(salePdfDto.getCreatedDate()));
        params.put("hora", DateUtil.formatToHourMinute(salePdfDto.getCreatedDate()));
        params.put("descuento", String.valueOf(salePdfDto.getDiscount()));
        params.put("igv", String.valueOf(salePdfDto.getIgv()));
        params.put("subTotal", String.valueOf(salePdfDto.getSubTotal()));
        params.put("totalPagar", String.valueOf(salePdfDto.getTotal()));
        params.put("listProducts", salePdfDto.getSaleDetailPdfDtoList());
        params.put("urlImage", salePdfDto.getImageUrl());
        params.put("vendedor", salePdfDto.getSeller());

        String stringMoneda = salePdfDto.getMoneyType() != null ? salePdfDto.getMoneyType().equalsIgnoreCase("USD") ? "Dólares Americanos" : salePdfDto.getMoneyType().equalsIgnoreCase("EUR") ? "Euros" :"Soles" : "Soles";

        params.put("montoLetras", MoneyUtil.convertir(salePdfDto.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString(), stringMoneda, true));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            InputStream jasperStream = getJasperCotizacionTemplateFromStorage();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            return out.toByteArray();
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            throw new Exception("Ocurrio un error al generar el PDF");
        }
    }

    InputStream getJasperCotizacionTemplateFromStorage() {
        try {
            ClassPathResource resource = new ClassPathResource("templates/report/sale-ticket.jrxml");

            InputStream jrxmlStream = resource.getInputStream();

            ByteArrayOutputStream jasperOutputStream = new ByteArrayOutputStream();

            JasperCompileManager.compileReportToStream(jrxmlStream, jasperOutputStream);
            return new ByteArrayInputStream(jasperOutputStream.toByteArray());

        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en plantilla Jasper", e);
        }
    }

    private String getSerieAndNumber(String value) {

        if (value == null || !value.contains("-")) {
            return value;
        }

        String[] parts = value.split("-", 3);

        if (parts.length < 3) {
            return value;
        }

        return parts[1] + "-" + parts[2];
    }
}
