package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ApisPeruDataDto;
import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.rest.ApisPeruRestTemplate;
import com.example.ventas_bodega.service.ApisPeruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApisPeruServiceImpl implements ApisPeruService {

    private final ApisPeruRestTemplate apisPeruRestTemplate;

    @Autowired
    public ApisPeruServiceImpl(ApisPeruRestTemplate apisPeruRestTemplate) {
        this.apisPeruRestTemplate = apisPeruRestTemplate;
    }

    @Override
    public ReniecDataDto getClienTDataByDni(String dni) {
        ReniecDataDto reniecDataDto = apisPeruRestTemplate.consultarPorDNI(dni);
        return reniecDataDto;
    }

    @Override
    public ApisPeruDataDto getInfoByRuc(String ruc) {
        return null;
    }
}
