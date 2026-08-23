package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ApisPeruDataDto;
import com.example.ventas_bodega.dto.ReniecDataDto;

public interface ApisPeruRestTemplate {

    ReniecDataDto consultarPorDNI(String dni);
    ApisPeruDataDto getInfoByRuc(String ruc);

}
