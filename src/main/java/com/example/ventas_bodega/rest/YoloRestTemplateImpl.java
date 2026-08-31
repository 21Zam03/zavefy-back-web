package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.response.YoloDetectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class YoloRestTemplateImpl implements YoloRestTemplate {

    private final RestTemplate restTemplate;
    private final String detectUrl;

    @Autowired
    public YoloRestTemplateImpl(RestTemplate restTemplate,
                                 @Value("${yolo.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.detectUrl = baseUrl + "/detect";
    }

    @Override
    public YoloDetectResponse detect(MultipartFile file) {

        ByteArrayResource fileResource;
        try {
            fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(detectUrl, requestEntity, YoloDetectResponse.class);
    }

}
