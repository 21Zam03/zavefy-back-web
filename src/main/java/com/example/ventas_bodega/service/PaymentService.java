package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.PaymentDto;
import com.example.ventas_bodega.request.PaymentRequest;
import com.example.ventas_bodega.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentResponse receivePayment(PaymentRequest request);
    Page<PaymentDto> getPayments(Long companyId, String source, String status, Pageable pageable);
    PaymentDto verifyPayment(Long paymentId, String securityCode, Long companyId);

}
