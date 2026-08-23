package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ApisPeruDataDto;
import com.example.ventas_bodega.dto.ReniecDataDto;

public interface ApisPeruService {

    ReniecDataDto getClienTDataByDni(String dni);
    ApisPeruDataDto getInfoByRuc(String ruc);

}
