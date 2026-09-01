package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.PaymentDto;
import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.enums.PaymentStatus;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.PaymentMapper;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.PaymentRepository;
import com.example.ventas_bodega.request.PaymentRequest;
import com.example.ventas_bodega.response.PaymentResponse;
import com.example.ventas_bodega.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, CompanyRepository companyRepository) {
        this.paymentRepository = paymentRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaymentResponse receivePayment(PaymentRequest request) {
        Long companyId = parseCompanyId(request.getCompanyId());

        if (!companyRepository.existsById(companyId)) {
            throw new NotFoundException("La empresa con id " + companyId + " no existe");
        }

        return paymentRepository.findByFingerprint(request.getFingerprint())
                .map(this::toDuplicateResponse)
                .orElseGet(() -> createPayment(request, companyId));
    }

    private PaymentResponse createPayment(PaymentRequest request, Long companyId) {
        PaymentEntity entity = PaymentMapper.requestToEntity(request, companyId);
        entity.setStatus(PaymentStatus.RECEIVED);
        try {
            PaymentEntity saved = paymentRepository.save(entity);
            return new PaymentResponse(true, "CREATED", saved.getId(), saved.getStatus().toString());
        } catch (DataIntegrityViolationException e) {
            // Condición de carrera: otra solicitud con el mismo fingerprint se guardó
            // entre el chequeo findByFingerprint y este save (protegido por el UNIQUE de BD)
            return paymentRepository.findByFingerprint(request.getFingerprint())
                    .map(this::toDuplicateResponse)
                    .orElseThrow(() -> e);
        }
    }

    private PaymentResponse toDuplicateResponse(PaymentEntity existing) {
        return new PaymentResponse(true, "DUPLICATE", existing.getId(), existing.getStatus().toString());
    }

    private Long parseCompanyId(String companyId) {
        try {
            return Long.parseLong(companyId);
        } catch (NumberFormatException e) {
            throw new BusinessException("companyId inválido: " + companyId);
        }
    }

    @Override
    public Page<PaymentDto> getPayments(Long companyId, String source, String status, Pageable pageable) {
        PaymentStatus paymentStatus = parseStatus(status);
        return paymentRepository.findByCompanyIdAndFilters(companyId, source, paymentStatus, pageable)
                .map(PaymentMapper::entityToDto);
    }

    private PaymentStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("status inválido: " + status);
        }
    }

}
