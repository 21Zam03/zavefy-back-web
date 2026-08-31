package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.YoloDetectionResultDto;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.mapper.YoloMapper;
import com.example.ventas_bodega.response.YoloDetectResponse;
import com.example.ventas_bodega.rest.YoloRestTemplate;
import com.example.ventas_bodega.service.YoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class YoloServiceImpl implements YoloService {

    private final YoloRestTemplate yoloRestTemplate;

    @Autowired
    public YoloServiceImpl(YoloRestTemplate yoloRestTemplate) {
        this.yoloRestTemplate = yoloRestTemplate;
    }

    @Override
    public YoloDetectionResultDto detectObjects(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException("Debe adjuntar una imagen para detectar objetos");
        }

        YoloDetectResponse response = yoloRestTemplate.detect(file);

        if (response == null) {
            throw new BusinessException("El servicio de detección no devolvió resultados");
        }

        return YoloMapper.responseToDto(response);
    }

}
