package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Slice test contra una H2 embebida (via @DataJpaTest), no contra la BD real del proyecto.
 * Verifica el comportamiento real de la query de PaymentRepository: aislamiento por empresa,
 * filtros opcionales, orden y paginación.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentEntity buildPayment(Long companyId, String source, PaymentStatus status,
                                        LocalDateTime receivedAt, String fingerprint) {
        PaymentEntity entity = new PaymentEntity();
        entity.setCompanyId(companyId);
        entity.setSource(source);
        entity.setNotificationId(1L);
        entity.setSenderName("Test Sender");
        entity.setAmount(new BigDecimal("10.00"));
        entity.setSecurityCode("123");
        entity.setReceivedAt(receivedAt);
        entity.setFingerprint(fingerprint);
        entity.setStatus(status);
        return entity;
    }

    @Test
    void debeRetornarSoloPagosDeLaEmpresaAutenticada() {
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-1"));
        paymentRepository.save(buildPayment(2L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-2"));

        Page<PaymentEntity> page = paymentRepository.findByCompanyIdAndFilters(1L, null, null, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getCompanyId()).isEqualTo(1L);
    }

    @Test
    void noDebeIncluirPagosDeOtraEmpresa() {
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-1"));
        paymentRepository.save(buildPayment(2L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-2"));

        Page<PaymentEntity> page = paymentRepository.findByCompanyIdAndFilters(2L, null, null, PageRequest.of(0, 10));

        assertThat(page.getContent()).extracting(PaymentEntity::getCompanyId).containsOnly(2L);
    }

    @Test
    void debeFiltrarPorSource() {
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-1"));
        paymentRepository.save(buildPayment(1L, "PLIN", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-2"));

        Page<PaymentEntity> page = paymentRepository.findByCompanyIdAndFilters(1L, "YAPE", null, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getSource()).isEqualTo("YAPE");
    }

    @Test
    void debeFiltrarPorStatus() {
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, LocalDateTime.now(), "fp-1"));
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.CANCELLED, LocalDateTime.now(), "fp-2"));

        Page<PaymentEntity> page = paymentRepository.findByCompanyIdAndFilters(1L, null, PaymentStatus.RECEIVED, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getStatus()).isEqualTo(PaymentStatus.RECEIVED);
    }

    @Test
    void debeRespetarPaginacion() {
        for (int i = 0; i < 5; i++) {
            paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED,
                    LocalDateTime.now().minusMinutes(i), "fp-" + i));
        }

        Page<PaymentEntity> firstPage = paymentRepository.findByCompanyIdAndFilters(1L, null, null, PageRequest.of(0, 2));
        Page<PaymentEntity> secondPage = paymentRepository.findByCompanyIdAndFilters(1L, null, null, PageRequest.of(1, 2));

        assertThat(firstPage.getContent()).hasSize(2);
        assertThat(secondPage.getContent()).hasSize(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(5);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);
    }

    @Test
    void debeOrdenarPorReceivedAtDescendente() {
        LocalDateTime now = LocalDateTime.now();
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, now.minusHours(2), "fp-old"));
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, now, "fp-new"));
        paymentRepository.save(buildPayment(1L, "YAPE", PaymentStatus.RECEIVED, now.minusHours(1), "fp-mid"));

        Page<PaymentEntity> page = paymentRepository.findByCompanyIdAndFilters(1L, null, null, PageRequest.of(0, 10));

        List<String> fingerprints = page.getContent().stream().map(PaymentEntity::getFingerprint).toList();
        assertThat(fingerprints).containsExactly("fp-new", "fp-mid", "fp-old");
    }

}
