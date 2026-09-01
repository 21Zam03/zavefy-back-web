package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.PaymentDto;
import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.enums.PaymentStatus;
import com.example.ventas_bodega.exceptions.BusinessException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
        assertThat(response.getPaymentStatus()).isEqualTo("RECEIVED");

        ArgumentCaptor<PaymentEntity> captor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(paymentRepository).save(captor.capture());
        assertThat(captor.getValue().getCompanyId()).isEqualTo(123L);
        assertThat(captor.getValue().getFingerprint()).isEqualTo(request.getFingerprint());
        assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.RECEIVED);
    }

    @Test
    void receivePayment_conFingerprintExistente_debeRetornarDuplicateSinGuardarDeNuevo() {
        PaymentRequest request = buildValidRequest();

        PaymentEntity existing = new PaymentEntity();
        existing.setId(99L);
        existing.setFingerprint(request.getFingerprint());
        existing.setStatus(PaymentStatus.RECEIVED);

        when(companyRepository.existsById(123L)).thenReturn(true);
        when(paymentRepository.findByFingerprint(request.getFingerprint())).thenReturn(Optional.of(existing));

        PaymentResponse response = paymentService.receivePayment(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getStatus()).isEqualTo("DUPLICATE");
        assertThat(response.getPaymentId()).isEqualTo(99L);
        assertThat(response.getPaymentStatus()).isEqualTo("RECEIVED");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void receivePayment_conCarreraEnElUniqueDeBd_debeRetornarDuplicate() {
        PaymentRequest request = buildValidRequest();

        PaymentEntity existing = new PaymentEntity();
        existing.setId(55L);
        existing.setFingerprint(request.getFingerprint());
        existing.setStatus(PaymentStatus.RECEIVED);

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

    @Test
    void getPayments_debeDelegarEnElRepositorioConLosFiltrosYMapearADto() {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(1L);
        entity.setCompanyId(123L);
        entity.setSource("YAPE");
        entity.setNotificationId(456L);
        entity.setSenderName("Horst Zam*");
        entity.setAmount(new BigDecimal("1.00"));
        entity.setSecurityCode("342");
        entity.setReceivedAt(LocalDateTime.of(2026, 8, 31, 16, 1, 7));
        entity.setFingerprint("fp-1");
        entity.setStatus(PaymentStatus.RECEIVED);
        entity.setCreatedAt(LocalDateTime.of(2026, 8, 31, 16, 1, 10));

        Pageable pageable = PageRequest.of(0, 20);
        Page<PaymentEntity> repoPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(paymentRepository.findByCompanyIdAndFilters(eq(123L), eq("YAPE"), eq(PaymentStatus.RECEIVED), eq(pageable)))
                .thenReturn(repoPage);

        Page<PaymentDto> result = paymentService.getPayments(123L, "YAPE", "RECEIVED", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        PaymentDto dto = result.getContent().get(0);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getSource()).isEqualTo("YAPE");
        assertThat(dto.getStatus()).isEqualTo("RECEIVED");
    }

    @Test
    void getPayments_sinFiltros_debePasarNullAlRepositorio() {
        Pageable pageable = PageRequest.of(0, 20);
        when(paymentRepository.findByCompanyIdAndFilters(eq(123L), isNull(), isNull(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        paymentService.getPayments(123L, null, null, pageable);

        verify(paymentRepository).findByCompanyIdAndFilters(123L, null, null, pageable);
    }

    @Test
    void getPayments_conStatusInvalido_debeLanzarBusinessException() {
        Pageable pageable = PageRequest.of(0, 20);

        assertThatThrownBy(() -> paymentService.getPayments(123L, null, "NO_EXISTE", pageable))
                .isInstanceOf(BusinessException.class);

        verify(paymentRepository, never()).findByCompanyIdAndFilters(any(), any(), any(), any());
    }

    private PaymentEntity buildReceivedPayment(Long id, Long companyId, String securityCode) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setCompanyId(companyId);
        entity.setSource("YAPE");
        entity.setNotificationId(456L);
        entity.setSenderName("Horst Zam*");
        entity.setAmount(new BigDecimal("1.00"));
        entity.setSecurityCode(securityCode);
        entity.setReceivedAt(LocalDateTime.of(2026, 8, 31, 16, 1, 7));
        entity.setFingerprint("fp-1");
        entity.setStatus(PaymentStatus.RECEIVED);
        return entity;
    }

    @Test
    void verifyPayment_conCodigoCorrecto_debeCambiarEstadoAMatched() {
        PaymentEntity payment = buildReceivedPayment(1L, 123L, "342");

        when(paymentRepository.findByIdAndCompanyId(1L, 123L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentDto dto = paymentService.verifyPayment(1L, "342", 123L);

        assertThat(dto.getStatus()).isEqualTo("MATCHED");

        ArgumentCaptor<PaymentEntity> captor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(paymentRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.MATCHED);
    }

    @Test
    void verifyPayment_conCodigoIncorrecto_debeLanzarBusinessExceptionYNoGuardar() {
        PaymentEntity payment = buildReceivedPayment(1L, 123L, "342");

        when(paymentRepository.findByIdAndCompanyId(1L, 123L)).thenReturn(Optional.of(payment));

        assertThatThrownBy(() -> paymentService.verifyPayment(1L, "999", 123L))
                .isInstanceOf(BusinessException.class);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.RECEIVED);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void verifyPayment_conPagoDeOtraEmpresa_debeLanzarNotFoundException() {
        when(paymentRepository.findByIdAndCompanyId(1L, 999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.verifyPayment(1L, "342", 999L))
                .isInstanceOf(NotFoundException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void verifyPayment_conPagoYaVerificado_debeLanzarBusinessException() {
        PaymentEntity payment = buildReceivedPayment(1L, 123L, "342");
        payment.setStatus(PaymentStatus.MATCHED);

        when(paymentRepository.findByIdAndCompanyId(1L, 123L)).thenReturn(Optional.of(payment));

        assertThatThrownBy(() -> paymentService.verifyPayment(1L, "342", 123L))
                .isInstanceOf(BusinessException.class);

        verify(paymentRepository, never()).save(any());
    }

}
