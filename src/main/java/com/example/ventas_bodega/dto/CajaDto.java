package com.example.ventas_bodega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CajaDto {

    private Long id;
    private BigDecimal montoInicial;
    private String observacion;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private String estado;
    private BigDecimal montoContado;
    private BigDecimal montoEsperado;
    private BigDecimal diferencia;
    private String observacionCierre;
    private BigDecimal totalVentasEfectivo;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;

}
