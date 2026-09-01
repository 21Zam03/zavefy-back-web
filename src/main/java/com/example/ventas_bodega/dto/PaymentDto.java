package com.example.ventas_bodega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private String source;
    private Long notificationId;
    private String senderName;
    private BigDecimal amount;
    private String securityCode;
    private LocalDateTime receivedAt;
    private String status;
    private LocalDateTime createdAt;

}
