package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.PaymentRepository;
import com.example.ventas_bodega.request.PaymentRequest;
import com.example.ventas_bodega.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CompanyRepository companyRepository;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository, companyRepository);
    }

    private PaymentRequest buildValidRequest() {
        return new PaymentRequest(
                "123",
                "YAPE",
                456L,
                "Horst Zam*",
                new BigDecimal("1.00"),
                "342",
                LocalDateTime.of(2026, 8, 31, 16, 1, 7),
                "456|Horst Zam*|1.00|2026-08-31T16:01:07"
        );
    }

    @Test
    void receivePayment_conPagoNuevo_debeCrearlo() {
        PaymentRequest request = buildValidRequest();

        when(companyRepository.existsById(123L)).thenReturn(true);
        when(paymentRepository.findByFingerprint(request.getFingerprint())).thenReturn(Optional.empty());
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> {
            PaymentEntity entity = invocation.getArgument(0);
            entity.setId(10L);
            return entity;
        });

        PaymentResponse response = paymentService.receivePayment(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo("CREATED");
        assertThat(response.getPaymentId()).isEqualTo(10L);

        ArgumentCaptor<PaymentEntity> captor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(paymentRepository).save(captor.capture());
        assertThat(captor.getValue().getCompanyId()).isEqualTo(123L);
        assertThat(captor.getValue().getFingerprint()).isEqualTo(request.getFingerprint());
    }

    @Test
    void receivePayment_conFingerprintExistente_debeRetornarDuplicateSinGuardarDeNuevo() {
        PaymentRequest request = buildValidRequest();

        PaymentEntity existing = new PaymentEntity();
        existing.setId(99L);
        existing.setFingerprint(request.getFingerprint());

        when(companyRepository.existsById(123L)).thenReturn(true);
        when(paymentRepository.findByFingerprint(request.getFingerprint())).thenReturn(Optional.of(existing));

        PaymentResponse response = paymentService.receivePayment(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo("DUPLICATE");
        assertThat(response.getPaymentId()).isEqualTo(99L);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void receivePayment_conCarreraEnElUniqueDeBd_debeRetornarDuplicate() {
        PaymentRequest request = buildValidRequest();

        PaymentEntity existing = new PaymentEntity();
        existing.setId(55L);
        existing.setFingerprint(request.getFingerprint());

        when(companyRepository.existsById(123L)).thenReturn(true);
        when(paymentRepository.findByFingerprint(request.getFingerprint()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existing));
        when(paymentRepository.save(any(PaymentEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));

        PaymentResponse response = paymentService.receivePayment(request);

        assertThat(response.getStatus()).isEqualTo("DUPLICATE");
        assertThat(response.getPaymentId()).isEqualTo(55L);
    }

    @Test
    void receivePayment_conEmpresaInexistente_debeLanzarNotFoundException() {
        PaymentRequest request = buildValidRequest();

        when(companyRepository.existsById(123L)).thenReturn(false);

        assertThatThrownBy(() -> paymentService.receivePayment(request))
                .isInstanceOf(NotFoundException.class);

        verify(paymentRepository, never()).findByFingerprint(anyString());
        verify(paymentRepository, never()).save(any());
    }

}
