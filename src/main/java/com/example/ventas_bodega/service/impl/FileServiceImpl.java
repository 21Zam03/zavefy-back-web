package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.SaleDetailPdfDto;
import com.example.ventas_bodega.dto.SalePdfDto;
import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.SaleEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.SaleDetailRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.service.FileService;
import com.example.ventas_bodega.service.SaleService;
import com.example.ventas_bodega.util.DateUtil;
import com.example.ventas_bodega.util.MoneyUtil;
import com.example.ventas_bodega.util.QrUtil;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    private final SaleService saleService;
    private final ProductRepository productRepository;

    @Autowired
    public FileServiceImpl(SaleRepository saleRepository, CompanyRepository companyRepository, SaleDetailRepository saleDetailRepository, SaleService saleService, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.companyRepository = companyRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.saleService = saleService;
        this.productRepository = productRepository;
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
            salePdfDto.setSubTotalFinal(saleEntity.getSubTotalFinal());
            salePdfDto.setNotes(saleEntity.getNotes());
            salePdfDto.setSaleDetailPdfDtoList(saleDetailPdfDtoList);
            salePdfDto.setImageUrl(company.getImageUrl());
            salePdfDto.setType(saleEntity.getType());
            salePdfDto.setIdentifier(saleEntity.getIdentifier());
            salePdfDto.setSeller(saleEntity.getUser().getFirstname()+" "+saleEntity.getUser().getLastname());
            salePdfDto.setSaleLink(saleEntity.getSaleLink());
            return new ByteArrayInputStream(getByteFromPdfTicket(salePdfDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTicketToPrint(Long id) throws Exception {
        try {
            SaleEntity saleEntity = saleRepository.findById(id).orElseThrow(() -> {
                return new NotFoundException("SALE NOT FOUND");
            });
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
            salePdfDto.setSubTotalFinal(saleEntity.getSubTotalFinal());
            salePdfDto.setNotes(saleEntity.getNotes());
            salePdfDto.setSaleDetailPdfDtoList(saleDetailPdfDtoList);
            salePdfDto.setImageUrl(company.getImageUrl());
            salePdfDto.setType(saleEntity.getType());
            salePdfDto.setIdentifier(saleEntity.getIdentifier());
            salePdfDto.setSeller(saleEntity.getUser().getFirstname()+" "+saleEntity.getUser().getLastname());
            salePdfDto.setSaleLink(saleEntity.getSaleLink());
            return getTicketBytes(salePdfDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream getExcelSaleReport(String ruc, String type, String serial, Integer number, String fromDate, String toDate) throws Exception {
        try {
           List<SaleEntity> sales = saleRepository.findSalesByCompany(ruc, type, serial, number, fromDate, toDate);
            return new ByteArrayInputStream(getByteFromSaleList(sales));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream getPdfSaleReport(String ruc, String type, String serial, Integer number, String fromDate, String toDate) throws Exception {
        try {
            List<SaleEntity> sales = saleRepository.findSalesByCompany(ruc, type, serial, number, fromDate, toDate);
            return new ByteArrayInputStream(getByteFromSaleListForPDF(sales));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream getExcelProductReport(Long companyId, String barcode, String name, String stockStatus, Boolean active, Long categoryId) throws Exception {
        try {
            List<ProductEntity> products = productRepository.findProductsFromCompanyId(companyId, barcode, name, stockStatus, active, categoryId);
            return new ByteArrayInputStream(getByteFromProductListForEXCEL(products));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream getPdfProductReport(Long companyId, String barcode, String name, String stockStatus, Boolean active, Long categoryId) throws Exception {
        try {
            List<ProductEntity> products = productRepository.findProductsFromCompanyId(companyId, barcode, name, stockStatus, active, categoryId);
            return new ByteArrayInputStream(getByteFromProductListForPDF(products));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public byte[] getByteFromProductListForPDF(List<ProductEntity> products) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);

            document.open();

            // ===== TÍTULO =====
            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

            Paragraph title = new Paragraph("REPORTE DE PRODUCTOS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph(" "));

            // ===== TABLA =====
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            float[] widths = {3f, 5f, 2f, 2f, 2f, 3f, 2f};
            table.setWidths(widths);

            // ===== HEADER STYLE =====
            com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

            String[] headers = {
                    "Código",
                    "Nombre",
                    "Stock",
                    "Precio Unitario",
                    "Precio Venta",
                    "Categoría",
                    "Estado"
            };

            for (String h : headers) {

                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));

                cell.setBackgroundColor(new java.awt.Color(0, 102, 204)); // azul
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);

                table.addCell(cell);
            }

            // ===== BODY =====
            com.lowagie.text.Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            for (ProductEntity p : products) {

                table.addCell(new Phrase(
                        p.getBarcode() != null ? p.getBarcode() : "",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.getName() != null ? p.getName() : "",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.getStock() != null ? String.valueOf(p.getStock()) : "0",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.getUnitPrice() != null ? p.getUnitPrice().toString() : "0",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.getPrice() != null ? p.getPrice().toString() : "0",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.getCategoryEntity() != null
                                ? p.getCategoryEntity().getName()
                                : "",
                        bodyFont
                ));

                table.addCell(new Phrase(
                        p.isActive() ? "ACTIVO" : "INACTIVO",
                        bodyFont
                ));
            }

            document.add(table);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de productos", e);
        }
    }

    public byte[] getByteFromProductListForEXCEL(List<ProductEntity> products){
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Productos");

            // ===== ESTILO HEADER =====

            CellStyle headerStyle = workbook.createCellStyle();

            headerStyle.setFillForegroundColor(
                    IndexedColors.BLUE.getIndex()
            );

            headerStyle.setFillPattern(
                    FillPatternType.SOLID_FOREGROUND
            );

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(
                    IndexedColors.WHITE.getIndex()
            );

            headerStyle.setFont(headerFont);

            headerStyle.setAlignment(
                    HorizontalAlignment.CENTER
            );

            // ===== FORMATO MONEDA =====

            CellStyle moneyStyle = workbook.createCellStyle();

            DataFormat dataFormat = workbook.createDataFormat();

            moneyStyle.setDataFormat(
                    dataFormat.getFormat("#,##0.00")
            );

            // ===== HEADERS =====

            String[] columns = {
                    "Código Barras",
                    "Nombre",
                    "Descripción",
                    "Unidad Medida",
                    "Precio Unitario",
                    "Precio Venta",
                    "Stock",
                    "Estado Stock",
                    "Categoría",
                    "Activo",
                    "Fecha Registro"
            };

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {

                Cell cell = headerRow.createCell(i);

                cell.setCellValue(columns[i]);

                cell.setCellStyle(headerStyle);
            }

            // ===== DATA =====

            int rowNum = 1;

            for (ProductEntity product : products) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        product.getBarcode() != null
                                ? product.getBarcode()
                                : ""
                );

                row.createCell(1).setCellValue(
                        product.getName() != null
                                ? product.getName()
                                : ""
                );

                row.createCell(2).setCellValue(
                        product.getDescription() != null
                                ? product.getDescription()
                                : ""
                );

                row.createCell(3).setCellValue(
                        product.getMeasurementUnit() != null
                                ? product.getMeasurementUnit()
                                : ""
                );

                Cell unitPriceCell = row.createCell(4);

                unitPriceCell.setCellValue(
                        product.getUnitPrice() != null
                                ? product.getUnitPrice().doubleValue()
                                : 0
                );

                unitPriceCell.setCellStyle(moneyStyle);

                Cell salePriceCell = row.createCell(5);

                salePriceCell.setCellValue(
                        product.getPrice() != null
                                ? product.getPrice().doubleValue()
                                : 0
                );

                salePriceCell.setCellStyle(moneyStyle);

                row.createCell(6).setCellValue(
                        product.getStock() != null
                                ? product.getStock()
                                : 0
                );

                row.createCell(7).setCellValue(
                        getStockStatus(product.getStock())
                );

                row.createCell(8).setCellValue(
                        product.getCategoryEntity() != null
                                ? product.getCategoryEntity().getName()
                                : ""
                );

                row.createCell(9).setCellValue(
                        product.isActive()
                                ? "ACTIVO"
                                : "INACTIVO"
                );

                row.createCell(10).setCellValue(
                        product.getRegisterDate() != null
                                ? product.getRegisterDate()
                                : ""
                );
            }

            // ===== AJUSTAR COLUMNAS =====

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar header
            sheet.createFreezePane(0, 1);

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error generando Excel de productos",
                    e
            );
        }
    }

    public byte[] getByteFromSaleList(List<SaleEntity> sales) {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Ventas");

            // Header
            Row headerRow = sheet.createRow(0);

            String[] columns = {
                    "Tipo",
                    "Serie",
                    "Número",
                    "Cliente",
                    "Documento",
                    "Método Pago",
                    "Moneda",
                    "Subtotal",
                    "IGV",
                    "Total",
                    "Fecha"
            };

            CellStyle headerStyle = workbook.createCellStyle();

            Font font = workbook.createFont();
            font.setBold(true);

            headerStyle.setFont(font);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;

            for (SaleEntity sale : sales) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        sale.getType() != null ? sale.getType() : "");

                row.createCell(1).setCellValue(
                        sale.getSerial() != null ? sale.getSerial() : "");

                row.createCell(2).setCellValue(
                        sale.getNumber() != null ? sale.getNumber() : 0);

                row.createCell(3).setCellValue(
                        sale.getClientName() != null ? sale.getClientName() : "");

                row.createCell(4).setCellValue(
                        sale.getClientDocumentNumber() != null
                                ? sale.getClientDocumentNumber()
                                : "");

                row.createCell(5).setCellValue(
                        sale.getPaymentMethod() != null
                                ? sale.getPaymentMethod()
                                : "");

                row.createCell(6).setCellValue(
                        sale.getMoneyType() != null
                                ? sale.getMoneyType()
                                : "");

                row.createCell(7).setCellValue(
                        sale.getSubTotal() != null
                                ? sale.getSubTotal().doubleValue()
                                : 0);

                row.createCell(8).setCellValue(
                        sale.getIgv() != null
                                ? sale.getIgv().doubleValue()
                                : 0);

                row.createCell(9).setCellValue(
                        sale.getTotal() != null
                                ? sale.getTotal().doubleValue()
                                : 0);

                row.createCell(10).setCellValue(
                        sale.getRegisterDate() != null
                                ? sale.getRegisterDate()
                                : "");
            }

            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }

    public byte[] getByteFromSaleListForPDF(List<SaleEntity> sales) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate()); // horizontal tipo Excel
            PdfWriter.getInstance(document, out);

            document.open();

            // Título
            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph title = new Paragraph("REPORTE DE VENTAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // espacio

            // Tabla con columnas
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);

            float[] widths = {2f, 2f, 2f, 4f, 3f, 2f, 2f, 2f, 2f, 3f};
            table.setWidths(widths);

            // Header
            com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

            String[] headers = {
                    "Tipo", "Serie", "Número", "Cliente", "Documento",
                    "Moneda", "Subtotal", "IGV", "Total", "Fecha"
            };

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new java.awt.Color(0, 102, 204)); // azul
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Body
            com.lowagie.text.Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            for (SaleEntity s : sales) {

                table.addCell(new Phrase(s.getType(), bodyFont));
                table.addCell(new Phrase(s.getSerial(), bodyFont));
                table.addCell(new Phrase(String.valueOf(s.getNumber()), bodyFont));
                table.addCell(new Phrase(s.getClientName(), bodyFont));
                table.addCell(new Phrase(s.getClientDocumentNumber(), bodyFont));
                table.addCell(new Phrase(s.getMoneyType(), bodyFont));

                table.addCell(new Phrase(
                        String.valueOf(s.getSubTotal()), bodyFont));

                table.addCell(new Phrase(
                        String.valueOf(s.getIgv()), bodyFont));

                table.addCell(new Phrase(
                        String.valueOf(s.getTotal()), bodyFont));

                table.addCell(new Phrase(
                        String.valueOf(s.getRegisterDate()), bodyFont));
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
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
        params.put("subTotalFinal", String.valueOf(salePdfDto.getSubTotalFinal()));
        params.put("totalPagar", String.valueOf(salePdfDto.getTotal()));
        params.put("listProducts", salePdfDto.getSaleDetailPdfDtoList());
        params.put("urlImage", salePdfDto.getImageUrl());
        params.put("vendedor", salePdfDto.getSeller());
        System.out.println("DATA: "+ salePdfDto.getSaleLink());
        params.put("qr", new ByteArrayInputStream(QrUtil.generateQr(salePdfDto.getSaleLink(), 300,300)));

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

    public String getTicketBytes(SalePdfDto sale) throws Exception {
        StringBuilder ticket = new StringBuilder();

        ticket.append(center(sale.getComertialName().toUpperCase()));
        ticket.append("\n");

        ticket.append(center(sale.getRuc()));
        ticket.append("\n");

        ticket.append(center(sale.getAddress()));
        ticket.append("\n");

        ticket.append("--------------------------------\n");

        ticket.append("DOC: ");
        ticket.append(getSerieAndNumber(sale.getIdentifier()));
        ticket.append("\n");

        ticket.append("CLIENTE: ");
        ticket.append(sale.getClientName());
        ticket.append("\n");

        ticket.append("--------------------------------\n");

        for (SaleDetailPdfDto item : sale.getSaleDetailPdfDtoList()) {

            ticket.append(item.getProductName());
            ticket.append("\n");

            ticket.append(item.getQuantity())
                    .append(" x ")
                    .append(item.getUnitPrice())
                    .append(" = ")
                    .append(item.getTotalPrice());

            ticket.append("\n");
        }

        ticket.append("--------------------------------\n");

        ticket.append("SUBTOTAL: ");
        ticket.append(sale.getSubTotal());
        ticket.append("\n");

        ticket.append("IGV: ");
        ticket.append(sale.getIgv());
        ticket.append("\n");

        ticket.append("TOTAL: ");
        ticket.append(sale.getTotal());
        ticket.append("\n");

        ticket.append("--------------------------------\n");

        ticket.append(MoneyUtil.convertir(
                sale.getTotal().toString(),
                "Soles",
                true
        ));

        ticket.append("\n");

        ticket.append("GRACIAS POR SU COMPRA\n");

        ticket.append("\n\n\n\n\n\n"); // espacio para que salga el ticket
        ticket.append("\u001D\u0056\u0000"); // CUT

        return ticket.toString();
    }

    private String center(String text) {
        int width = 48;

        if (text.length() >= width) {
            return text;
        }

        int padding = (width - text.length()) / 2;

        return " ".repeat(padding) + text;
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

    private String getStockStatus(Integer stock) {

        if (stock == null || stock == 0) {
            return "SIN STOCK";
        }

        if (stock <= 10) {
            return "BAJO STOCK";
        }

        if (stock <= 50) {
            return "STOCK MODERADO";
        }

        return "STOCK SUFICIENTE";
    }

}
