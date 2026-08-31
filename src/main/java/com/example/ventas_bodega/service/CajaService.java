package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.CajaDto;
import com.example.ventas_bodega.dto.CajaMovimientoDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.AbrirMovimientoCajaRequest;
import com.example.ventas_bodega.request.CerrarCajaRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CajaService {

    CajaDto getSesionActual(String ruc);
    CajaDto abrirCaja(CajaDto cajaDto, UserEntity user);
    List<CajaMovimientoDto> getMovimientos(String ruc);
    CajaMovimientoDto registrarMovimiento(AbrirMovimientoCajaRequest request, UserEntity user);
    CajaDto cerrarCaja(CerrarCajaRequest request, UserEntity user);
    Page<CajaDto> getHistorial(String ruc, Pageable pageable);

}
