package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.PaymentDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.PaymentRequest;
import com.example.ventas_bodega.request.VerifyPaymentRequest;
import com.example.ventas_bodega.response.PaymentResponse;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PaymentController.API_PATH)
public class PaymentController {

    public static final String API_PATH = "/api/payments";

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> receivePayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.receivePayment(request);
        HttpStatus status = "CREATED".equals(response.getStatus()) ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping
    public ResponseEntity<?> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String status,
            @CurrentUser UserEntity user
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Long companyId = user.getCompany().getCompanyId();
        return new ResponseEntity<>(paymentService.getPayments(companyId, source, status, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<?> verifyPayment(
            @PathVariable Long id,
            @Valid @RequestBody VerifyPaymentRequest request,
            @CurrentUser UserEntity user
    ) {
        Long companyId = user.getCompany().getCompanyId();
        PaymentDto dto = paymentService.verifyPayment(id, request.getSecurityCode(), companyId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
