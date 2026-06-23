package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.FileService;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(FileController.API_PATH)
public class FileController {

    public static final String API_PATH = "/api/file";
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download-ticket/{id}")
    public ResponseEntity<?> downloadTicket(@PathVariable Long id) throws Exception {
        byte[] targetArray = ByteStreams.toByteArray(fileService.getPdfTicket(id));
        ByteArrayResource resource = new ByteArrayResource(targetArray);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }

    @GetMapping("/print-ticket/{id}")
    public ResponseEntity<?> printTicket(@PathVariable Long id) throws Exception {
        String ticket = fileService.getTicketToPrint(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/sale/excel-report")
    public ResponseEntity<?> getExcelReport(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String serial,
            @RequestParam(required = false) Integer number,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) throws Exception {
        byte[] targetArray = ByteStreams.toByteArray(fileService.getExcelSaleReport(user.getCompany().getRuc(), type, serial, number, fromDate, toDate));
        ByteArrayResource resource = new ByteArrayResource(targetArray);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @GetMapping("/sale/pdf-report")
    public ResponseEntity<?> getPdfReport(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String serial,
            @RequestParam(required = false) Integer number,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) throws Exception {
        byte[] targetArray = ByteStreams.toByteArray(fileService.getPdfSaleReport(user.getCompany().getRuc(), type, serial, number, fromDate, toDate));
        ByteArrayResource resource = new ByteArrayResource(targetArray);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=reporte-ventas.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
