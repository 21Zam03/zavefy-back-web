package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.service.PrintService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PrintServiceImpl implements PrintService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendToPrint(String ticket) {

        String url = "http://localhost:8081/print";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> request = new HttpEntity<>(ticket, headers);

        restTemplate.postForEntity(url, request, String.class);
    }

}
