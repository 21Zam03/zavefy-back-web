package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.PaymentEntity;
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
                .map(existing -> new PaymentResponse(true, "DUPLICATE", existing.getId()))
                .orElseGet(() -> createPayment(request, companyId));
    }

    private PaymentResponse createPayment(PaymentRequest request, Long companyId) {
        PaymentEntity entity = PaymentMapper.requestToEntity(request, companyId);
        try {
            PaymentEntity saved = paymentRepository.save(entity);
            return new PaymentResponse(true, "CREATED", saved.getId());
        } catch (DataIntegrityViolationException e) {
            // Condición de carrera: otra solicitud con el mismo fingerprint se guardó
            // entre el chequeo findByFingerprint y este save (protegido por el UNIQUE de BD)
            return paymentRepository.findByFingerprint(request.getFingerprint())
                    .map(existing -> new PaymentResponse(true, "DUPLICATE", existing.getId()))
                    .orElseThrow(() -> e);
        }
    }

    private Long parseCompanyId(String companyId) {
        try {
            return Long.parseLong(companyId);
        } catch (NumberFormatException e) {
            throw new BusinessException("companyId inválido: " + companyId);
        }
    }

}
