package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateReceivablePaymentRequest;
import com.example.ventas_bodega.request.CreateReceivableRequest;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ReceivableController.API_PATH)
public class ReceivableController {

    public static final String API_PATH = "/api/receivable";

    private final ReceivableService receivableService;

    @Autowired
    public ReceivableController(ReceivableService receivableService) {
        this.receivableService = receivableService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getReceivablesByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer clientId,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(
                receivableService.getReceivablesByCompany(user, searchKey, status, clientId, page, size),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<?> createReceivable(@RequestBody CreateReceivableRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(receivableService.createReceivable(request, user), HttpStatus.CREATED);
    }

    @PostMapping("/{receivableId}/payments")
    public ResponseEntity<?> registerPayment(
            @PathVariable Long receivableId,
            @RequestBody CreateReceivablePaymentRequest request,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(receivableService.registerPayment(receivableId, request, user), HttpStatus.OK);
    }

    @GetMapping("/payments/all")
    public ResponseEntity<?> getPaymentsByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(
                receivableService.getPaymentsByCompany(user, searchKey, paymentMethod, clientId, fromDate, toDate, page, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{receivableId}/payments")
    public ResponseEntity<?> getPaymentsByReceivable(
            @PathVariable Long receivableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity<>(receivableService.getPaymentsByReceivable(receivableId, user, page, size), HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}/balance")
    public ResponseEntity<?> getClientBalance(@PathVariable Integer clientId, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(receivableService.getClientBalance(clientId, user), HttpStatus.OK);
    }

}
