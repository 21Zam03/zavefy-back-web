package com.example.ventas_bodega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CajaMovimientoDto {

    private Long id;
    private String tipo;
    private BigDecimal monto;
    private String motivo;
    private LocalDateTime fecha;

}
