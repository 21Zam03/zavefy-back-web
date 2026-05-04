package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.rest.ReniecRestTemplate;
import com.example.ventas_bodega.service.ReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReniecServiceImpl implements ReniecService {

    private final ReniecRestTemplate reniecRestTemplate;

    @Autowired
    public ReniecServiceImpl(ReniecRestTemplate reniecRestTemplate) {
        this.reniecRestTemplate = reniecRestTemplate;
    }

    @Override
    public ReniecDataDto getClienTDataByDni(String dni) {
        ReniecDataDto reniecDataDto = reniecRestTemplate.consultarPorDNI(dni);
        return reniecDataDto;
    }
}
