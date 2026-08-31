package com.example.ventas_bodega.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CerrarCajaRequest {

    @NotNull(message = "El monto contado es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto contado no puede ser negativo")
    private BigDecimal montoContado;

    @Size(max = 255, message = "La observación no puede superar los 255 caracteres")
    private String observacionCierre;

}
