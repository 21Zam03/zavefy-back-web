package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CajaDto;
import com.example.ventas_bodega.dto.CajaMovimientoDto;
import com.example.ventas_bodega.entity.CajaEntity;
import com.example.ventas_bodega.entity.CajaMovimientoEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.enums.CajaMovimientoTipoEnum;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.CajaMapper;
import com.example.ventas_bodega.mapper.CajaMovimientoMapper;
import com.example.ventas_bodega.repository.CajaMovimientoRepository;
import com.example.ventas_bodega.repository.CajaRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.repository.SalePaymentLineRepository;
import com.example.ventas_bodega.response.MessageResponse;
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
    private final SalePaymentLineRepository salePaymentLineRepository;

    @Autowired
    public CajaServiceImpl(CajaRepository cajaRepository, CajaMovimientoRepository cajaMovimientoRepository, SaleRepository saleRepository, SalePaymentLineRepository salePaymentLineRepository) {
        this.cajaRepository = cajaRepository;
        this.cajaMovimientoRepository = cajaMovimientoRepository;
        this.saleRepository = saleRepository;
        this.salePaymentLineRepository = salePaymentLineRepository;
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
        movimiento.setCategoria(request.getCategoria());

        return CajaMovimientoMapper.entityToDto(cajaMovimientoRepository.save(movimiento));
    }

    @Override
    public CajaMovimientoDto actualizarMovimiento(Long movimientoId, AbrirMovimientoCajaRequest request, UserEntity user) {
        CajaMovimientoEntity movimiento = obtenerMovimientoDeCajaAbierta(movimientoId, user);
        movimiento.setTipo(request.getTipo());
        movimiento.setMonto(request.getMonto());
        movimiento.setMotivo(request.getMotivo());
        movimiento.setCategoria(request.getCategoria());
        return CajaMovimientoMapper.entityToDto(cajaMovimientoRepository.save(movimiento));
    }

    @Override
    public MessageResponse eliminarMovimiento(Long movimientoId, UserEntity user) {
        CajaMovimientoEntity movimiento = obtenerMovimientoDeCajaAbierta(movimientoId, user);
        cajaMovimientoRepository.delete(movimiento);
        return new MessageResponse("Movimiento eliminado", true);
    }

    // Solo se pueden editar/eliminar movimientos de la caja actualmente abierta: una vez
    // cerrada, montoEsperado/diferencia ya quedaron calculados con esos movimientos —
    // tocarlos después dejaría ese cuadre desincronizado en silencio (mismo criterio ya
    // aplicado a "Anular venta").
    private CajaMovimientoEntity obtenerMovimientoDeCajaAbierta(Long movimientoId, UserEntity user) {
        CajaEntity cajaAbierta = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(user.getCompany().getRuc())
                .orElseThrow(() -> new BusinessException("No hay una caja abierta."));

        CajaMovimientoEntity movimiento = cajaMovimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new NotFoundException("El movimiento no existe"));

        if (!movimiento.getCaja().getId().equals(cajaAbierta.getId())) {
            throw new BusinessException("Solo se pueden editar o eliminar movimientos de la caja actualmente abierta");
        }

        return movimiento;
    }

    @Override
    public CajaDto cerrarCaja(CerrarCajaRequest request, UserEntity user) {
        CajaEntity caja = cajaRepository
                .findFirstByUser_Company_RucAndFechaCierreIsNullOrderByFechaAperturaDesc(user.getCompany().getRuc())
                .orElseThrow(() -> new BusinessException("No hay una caja abierta"));

        // Ventas 100% efectivo + la parte en efectivo de ventas con pago dividido (ambas
        // consultas ya excluyen ventas anuladas).
        BigDecimal totalVentasEfectivo = saleRepository.sumTotalEfectivoByCajaId(caja.getId())
                .add(salePaymentLineRepository.sumEfectivoByCajaId(caja.getId()));
        BigDecimal totalIngresos = cajaMovimientoRepository.sumMontoByCajaIdAndTipo(caja.getId(), CajaMovimientoTipoEnum.INGRESO);
        BigDecimal totalEgresos = cajaMovimientoRepository.sumMontoByCajaIdAndTipo(caja.getId(), CajaMovimientoTipoEnum.EGRESO);

        BigDecimal montoEsperado = caja.getMontoInicial().add(totalVentasEfectivo).add(totalIngresos).subtract(totalEgresos);
        BigDecimal diferencia = request.getMontoContado().subtract(montoEsperado);

        caja.setFechaCierre(LocalDateTime.now());
        caja.setMontoContado(request.getMontoContado());
        caja.setMontoEsperado(montoEsperado);
        caja.setDiferencia(diferencia);
        caja.setObservacionCierre(request.getObservacionCierre());
        // Se persisten (no solo se devuelven en la respuesta) para que el historial y el
        // recibo de cierre puedan mostrar el desglose completo después, no solo en el momento.
        caja.setTotalVentasEfectivo(totalVentasEfectivo);
        caja.setTotalIngresos(totalIngresos);
        caja.setTotalEgresos(totalEgresos);

        return CajaMapper.entityToDto(cajaRepository.save(caja));
    }

    @Override
    public Page<CajaDto> getHistorial(String ruc, Pageable pageable) {
        return cajaRepository.findByUser_Company_RucAndFechaCierreIsNotNullOrderByFechaCierreDesc(ruc, pageable)
                .map(CajaMapper::entityToDto);
    }

}
