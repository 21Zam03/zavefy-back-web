package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CajaMovimientoDto;
import com.example.ventas_bodega.entity.CajaMovimientoEntity;

public class CajaMovimientoMapper {

    public static CajaMovimientoDto entityToDto(CajaMovimientoEntity entity) {
        CajaMovimientoDto dto = new CajaMovimientoDto();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo().toString());
        dto.setMonto(entity.getMonto());
        dto.setMotivo(entity.getMotivo());
        dto.setFecha(entity.getFecha());
        return dto;
    }

}
