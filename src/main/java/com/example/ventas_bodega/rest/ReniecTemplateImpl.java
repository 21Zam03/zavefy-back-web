package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.mapper.ReniecDataMapper;
import com.example.ventas_bodega.response.ReniecDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecTemplateImpl implements ReniecRestTemplate {

    private final String BASE_URL = "http://190.216.114.149:8080/";
    private final RestTemplate restTemplate;

    @Autowired
    public ReniecTemplateImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ReniecDataDto consultarPorDNI(String dni) {

        String url = BASE_URL + "/ServicioRENIECEx/reniec/consultarPorDNI/" + dni;

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "PostmanRuntime");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ReniecDataResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ReniecDataResponse.class
        );

        if (response.getBody() != null) {
            return ReniecDataMapper.responseToDto(response.getBody());
        }

        return null;
    }

}
