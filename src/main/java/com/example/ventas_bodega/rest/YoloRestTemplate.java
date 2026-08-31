package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.response.YoloDetectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface YoloRestTemplate {

    YoloDetectResponse detect(MultipartFile file);

}
