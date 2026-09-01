package com.example.ventas_bodega.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPaymentRequest {

    @NotBlank(message = "El código de verificación es obligatorio")
    @Pattern(regexp = "^\\d{3}$", message = "El código de verificación debe tener exactamente 3 dígitos")
    private String securityCode;

}
