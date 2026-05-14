package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.service.FileService;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
