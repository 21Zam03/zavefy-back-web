package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.ReceivableDto;
import com.example.ventas_bodega.dto.ReceivablePaymentDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateReceivablePaymentRequest;
import com.example.ventas_bodega.request.CreateReceivableRequest;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ReceivableService {

    Page<ReceivableDto> getReceivablesByCompany(UserEntity user, String searchKey, String status, Integer clientId, int page, int size);
    MessageResponse createReceivable(CreateReceivableRequest request, UserEntity user);
    MessageResponse createFromSale(Integer saleId, Integer clientId, BigDecimal pendingBalance, UserEntity user);
    MessageResponse registerPayment(Long receivableId, CreateReceivablePaymentRequest request, UserEntity user);
    Page<ReceivablePaymentDto> getPaymentsByReceivable(Long receivableId, UserEntity user, int page, int size);
    Page<ReceivablePaymentDto> getPaymentsByCompany(UserEntity user, String searchKey, String paymentMethod, Integer clientId, String fromDate, String toDate, int page, int size);
    BigDecimal getClientBalance(Integer clientId, UserEntity user);

}
