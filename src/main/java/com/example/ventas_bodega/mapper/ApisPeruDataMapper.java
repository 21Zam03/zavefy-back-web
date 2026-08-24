package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ApisPeruDataDto;
import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.response.ApisPeruDataDniResponse;
import com.example.ventas_bodega.response.ApisPeruDataRucResponse;

public class ApisPeruDataMapper {

    public static ReniecDataDto responseDniToDto(ApisPeruDataDniResponse apisPeruDataDniResponse) {
        ReniecDataDto dto = new ReniecDataDto();
        dto.setNombres(apisPeruDataDniResponse.getNombres());
        dto.setApellidoMaterno(apisPeruDataDniResponse.getApellidoMaterno());
        dto.setApellidoPaterno(apisPeruDataDniResponse.getApellidoPaterno());
        dto.setNumeroDNI(apisPeruDataDniResponse.getDni());
        return dto;
    }

    public static ApisPeruDataDto responseRucToDto(ApisPeruDataRucResponse apisPeruDataRucResponse) {
        ApisPeruDataDto dto = new ApisPeruDataDto();
        dto.setRuc(apisPeruDataRucResponse.getRuc());
        dto.setDireccion(apisPeruDataRucResponse.getDireccion());
        dto.setNombreComercial(apisPeruDataRucResponse.getNombreComercial());
        dto.setRazonSocial(apisPeruDataRucResponse.getRazonSocial());
        return dto;
    }

}
