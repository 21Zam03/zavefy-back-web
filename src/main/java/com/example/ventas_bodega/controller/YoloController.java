package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.YoloDetectionResultDto;
import com.example.ventas_bodega.service.YoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(YoloController.API_PATH)
public class YoloController {

    public static final String API_PATH = "/api/yolo";
    private final YoloService yoloService;

    @Autowired
    public YoloController(YoloService yoloService) {
        this.yoloService = yoloService;
    }

    @PostMapping("/detect")
    public ResponseEntity<YoloDetectionResultDto> detect(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(yoloService.detectObjects(file), HttpStatus.OK);
    }

}
