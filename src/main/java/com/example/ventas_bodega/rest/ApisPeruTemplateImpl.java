package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ApisPeruDataDto;
import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.mapper.ApisPeruDataMapper;
import com.example.ventas_bodega.response.ApisPeruDataDniResponse;
import com.example.ventas_bodega.response.ApisPeruDataRucResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApisPeruTemplateImpl implements ApisPeruRestTemplate {

    private final String BASE_URL_DNI = "https://dniruc.apisperu.com/api/v1/dni";
    private final String BASE_URL_RUC = "https://dniruc.apisperu.com/api/v1/ruc";

    private final RestTemplate restTemplate;

    @Autowired
    public ApisPeruTemplateImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ReniecDataDto consultarPorDNI(String dni) {

        String url = BASE_URL_DNI + "/" + dni + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjIxemFtMDMuZnJlZUBnbWFpbC5jb20ifQ.uOXXTqAaGgu6ZYcQy3Y4WkYxJZldHV18tFD_fif0uJo";

        HttpHeaders headers = new HttpHeaders();
        //headers.set("User-Agent", "PostmanRuntime");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApisPeruDataDniResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ApisPeruDataDniResponse.class
        );

        if (response.getBody() != null) {
            return ApisPeruDataMapper.responseDniToDto(response.getBody());
        }

        return null;
    }

    @Override
    public ApisPeruDataDto getInfoByRuc(String ruc) {
        String url = BASE_URL_RUC + "/" + ruc + "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjIxemFtMDMuZnJlZUBnbWFpbC5jb20ifQ.uOXXTqAaGgu6ZYcQy3Y4WkYxJZldHV18tFD_fif0uJo";
        HttpHeaders headers = new HttpHeaders();
        //headers.set("User-Agent", "PostmanRuntime");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApisPeruDataRucResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ApisPeruDataRucResponse.class
        );

        if (response.getBody() != null) {
            return ApisPeruDataMapper.responseRucToDto(response.getBody());
        }

        return null;
    }


}
