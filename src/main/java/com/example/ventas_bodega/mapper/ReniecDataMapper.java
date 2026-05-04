package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.response.ReniecDataResponse;

public class ReniecDataMapper {

    public static ReniecDataDto responseToDto(ReniecDataResponse reniecDataResponse) {
        ReniecDataDto dto = new ReniecDataDto();
        dto.setNombres(reniecDataResponse.getNombres());
        dto.setApellidoMaterno(reniecDataResponse.getApellidoMaterno());
        dto.setApellidoPaterno(reniecDataResponse.getApellidoPaterno());
        dto.setNumeroDNI(reniecDataResponse.getNumeroDNI());
        return dto;
    }

}
