package com.example.ventas_bodega.rest;

import com.example.ventas_bodega.dto.ReniecDataDto;

public interface ReniecRestTemplate {

    ReniecDataDto consultarPorDNI(String dni);

}
