package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CajaDto;
import com.example.ventas_bodega.dto.CajaMovimientoDto;
import com.example.ventas_bodega.entity.CajaEntity;
import com.example.ventas_bodega.entity.CajaMovimientoEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.enums.CajaMovimientoTipoEnum;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.mapper.CajaMapper;
import com.example.ventas_bodega.mapper.CajaMovimientoMapper;
import com.example.ventas_bodega.repository.CajaMovimientoRepository;
import com.example.ventas_bodega.repository.CajaRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.request.AbrirMovimientoCajaRequest;
import com.example.ventas_bodega.request.CerrarCajaRequest;
import com.example.ventas_bodega.service.CajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepository;
    private final CajaMovimientoRepository cajaMovimientoRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public CajaServiceImpl(CajaRepository cajaRepository, CajaMovimientoRepository cajaMovimientoRepository, SaleRepository saleRepository) {
        this.cajaRepository = cajaRepository;
        this.cajaMovimientoRepository = cajaMovimientoRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public CajaDto getSesionActual(String ruc) {
        return cajaRepository.findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(ruc)
                .map(CajaMapper::entityToDto)
                .orElse(null);
    }

    @Override
    public CajaDto abrirCaja(CajaDto cajaDto, UserEntity user) {
        cajaRepository.findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(user.getCompany().getRuc())
                .ifPresent(caja -> {
                    throw new BusinessException("Ya existe una caja abierta");
                });

        CajaEntity cajaEntity = CajaMapper.dtoToEntity(cajaDto, user);
        cajaEntity = cajaRepository.save(cajaEntity);
        return CajaMapper.entityToDto(cajaEntity);
    }

    @Override
    public List<CajaMovimientoDto> getMovimientos(String ruc) {
        CajaEntity caja = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(ruc)
                .orElseThrow(() -> new BusinessException("No hay una caja abierta."));

        return cajaMovimientoRepository.findByCaja_IdOrderByFechaDesc(caja.getId())
                .stream()
                .map(CajaMovimientoMapper::entityToDto)
                .toList();
    }

    @Override
    public CajaMovimientoDto registrarMovimiento(AbrirMovimientoCajaRequest request, UserEntity user) {
        CajaEntity caja = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(user.getCompany().getRuc())
                .orElseThrow(() -> new BusinessException("No hay una caja abierta."));

        CajaMovimientoEntity movimiento = new CajaMovimientoEntity();
        movimiento.setCaja(caja);
        movimiento.setTipo(request.getTipo());
        movimiento.setMonto(request.getMonto());
        movimiento.setMotivo(request.getMotivo());

        return CajaMovimientoMapper.entityToDto(cajaMovimientoRepository.save(movimiento));
    }

    @Override
    public CajaDto cerrarCaja(CerrarCajaRequest request, UserEntity user) {
        CajaEntity caja = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(user.getCompany().getRuc())
                .orElseThrow(() -> new BusinessException("No hay una caja abierta"));

        BigDecimal totalVentasEfectivo = saleRepository.sumTotalEfectivoByCajaId(caja.getId());
        BigDecimal totalIngresos = cajaMovimientoRepository.sumMontoByCajaIdAndTipo(caja.getId(), CajaMovimientoTipoEnum.INGRESO);
        BigDecimal totalEgresos = cajaMovimientoRepository.sumMontoByCajaIdAndTipo(caja.getId(), CajaMovimientoTipoEnum.EGRESO);

        BigDecimal montoEsperado = caja.getMontoInicial().add(totalVentasEfectivo).add(totalIngresos).subtract(totalEgresos);
        BigDecimal diferencia = request.getMontoContado().subtract(montoEsperado);

        caja.setFechaCierre(LocalDateTime.now());
        caja.setMontoContado(request.getMontoContado());
        caja.setMontoEsperado(montoEsperado);
        caja.setDiferencia(diferencia);
        caja.setObservacionCierre(request.getObservacionCierre());

        CajaDto dto = CajaMapper.entityToDto(cajaRepository.save(caja));
        dto.setTotalVentasEfectivo(totalVentasEfectivo);
        dto.setTotalIngresos(totalIngresos);
        dto.setTotalEgresos(totalEgresos);
        return dto;
    }

    @Override
    public Page<CajaDto> getHistorial(String ruc, Pageable pageable) {
        return cajaRepository.findByUser_Company_RucAndFechaCierreIsNotNullOrderByFechaCierreDesc(ruc, pageable)
                .map(CajaMapper::entityToDto);
    }

}
