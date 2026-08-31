package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CajaDto;
import com.example.ventas_bodega.entity.CajaEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.AbrirCajaRequest;

public class CajaMapper {

    public static CajaDto requestToDto(AbrirCajaRequest request) {
        CajaDto cajaDto = new CajaDto();
        cajaDto.setMontoInicial(request.getMontoInicial());
        cajaDto.setObservacion(request.getObservacion());
        return cajaDto;
    }

    public static CajaEntity dtoToEntity(CajaDto cajaDto, UserEntity user) {
        CajaEntity cajaEntity = new CajaEntity();
        cajaEntity.setMontoInicial(cajaDto.getMontoInicial());
        cajaEntity.setObservacion(cajaDto.getObservacion());
        cajaEntity.setUser(user);
        return cajaEntity;
    }

    public static CajaDto entityToDto(CajaEntity cajaEntity) {
        CajaDto cajaDto = new CajaDto();
        cajaDto.setId(cajaEntity.getId());
        cajaDto.setMontoInicial(cajaEntity.getMontoInicial());
        cajaDto.setObservacion(cajaEntity.getObservacion());
        cajaDto.setFechaApertura(cajaEntity.getFechaApertura());
        cajaDto.setFechaCierre(cajaEntity.getFechaCierre());
        cajaDto.setEstado(cajaEntity.getFechaCierre() == null ? "ABIERTA" : "CERRADA");
        cajaDto.setMontoContado(cajaEntity.getMontoContado());
        cajaDto.setMontoEsperado(cajaEntity.getMontoEsperado());
        cajaDto.setDiferencia(cajaEntity.getDiferencia());
        cajaDto.setObservacionCierre(cajaEntity.getObservacionCierre());
        return cajaDto;
    }

}
