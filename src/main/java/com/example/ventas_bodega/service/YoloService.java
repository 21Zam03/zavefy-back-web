package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.YoloDetectionResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface YoloService {

    YoloDetectionResultDto detectObjects(MultipartFile file);

}
