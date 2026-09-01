package com.example.ventas_bodega.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private boolean success;
    private String status;
    private Long paymentId;
    private String paymentStatus;

}
