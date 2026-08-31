package com.example.ventas_bodega.service;

import com.example.ventas_bodega.request.PaymentRequest;
import com.example.ventas_bodega.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse receivePayment(PaymentRequest request);

}
