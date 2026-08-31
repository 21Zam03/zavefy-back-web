package com.example.ventas_bodega.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "El companyId es obligatorio")
    @Pattern(regexp = "^\\d+$", message = "El companyId debe ser numérico")
    private String companyId;

    @NotBlank(message = "El source es obligatorio")
    private String source;

    @NotNull(message = "El notificationId es obligatorio")
    private Long notificationId;

    @NotBlank(message = "El senderName es obligatorio")
    private String senderName;

    @NotNull(message = "El amount es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El amount debe ser mayor a 0")
    private BigDecimal amount;

    @NotBlank(message = "El securityCode es obligatorio")
    private String securityCode;

    @NotNull(message = "El receivedAt es obligatorio")
    private LocalDateTime receivedAt;

    @NotBlank(message = "El fingerprint es obligatorio")
    private String fingerprint;

}
