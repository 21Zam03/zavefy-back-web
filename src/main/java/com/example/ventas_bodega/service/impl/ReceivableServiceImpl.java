package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ReceivableDto;
import com.example.ventas_bodega.dto.ReceivablePaymentDto;
import com.example.ventas_bodega.dto.interfaces.ReceivableDtoInter;
import com.example.ventas_bodega.dto.interfaces.ReceivablePaymentDtoInter;
import com.example.ventas_bodega.entity.ClientEntity;
import com.example.ventas_bodega.entity.ReceivableEntity;
import com.example.ventas_bodega.entity.ReceivablePaymentEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.ReceivableMapper;
import com.example.ventas_bodega.mapper.ReceivablePaymentMapper;
import com.example.ventas_bodega.repository.ClientRepository;
import com.example.ventas_bodega.repository.ReceivablePaymentRepository;
import com.example.ventas_bodega.repository.ReceivableRepository;
import com.example.ventas_bodega.request.CreateReceivablePaymentRequest;
import com.example.ventas_bodega.request.CreateReceivableRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivableServiceImpl implements ReceivableService {

    private final ReceivableRepository receivableRepository;
    private final ReceivablePaymentRepository receivablePaymentRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ReceivableServiceImpl(
            ReceivableRepository receivableRepository,
            ReceivablePaymentRepository receivablePaymentRepository,
            ClientRepository clientRepository) {
        this.receivableRepository = receivableRepository;
        this.receivablePaymentRepository = receivablePaymentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Page<ReceivableDto> getReceivablesByCompany(UserEntity user, String searchKey, String status, Integer clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReceivableDtoInter> receivables = receivableRepository.findReceivablesByFilters(
                user.getCompany().getCompanyId(), searchKey, status, clientId, pageable
        );
        List<ReceivableDto> data = new ArrayList<>();
        for (int i = 0; i < receivables.getContent().size(); i++) {
            data.add(ReceivableMapper.mapInterfaceToDto(receivables.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, receivables.getTotalElements());
    }

    @Override
    @Transactional
    public MessageResponse createReceivable(CreateReceivableRequest request, UserEntity user) {
        if (request == null || request.getClientId() == null) {
            throw new IllegalArgumentException("El fiado no tiene cliente");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto del fiado debe ser mayor a 0");
        }

        ClientEntity client = clientRepository.findByClientIdAndCompanyId(request.getClientId(), user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El cliente no existe en su cartera"));

        ReceivableEntity receivableEntity = new ReceivableEntity();
        receivableEntity.setClient(client);
        receivableEntity.setSaleId(null);
        receivableEntity.setOriginalAmount(request.getAmount());
        receivableEntity.setBalance(request.getAmount());
        receivableEntity.setConcept(request.getConcept());
        receivableEntity.setDueDate(request.getDueDate());
        receivableEntity.setStatus("PENDIENTE");
        receivableEntity.setCompanyId(user.getCompany().getCompanyId());
        ReceivableEntity receivableCreated = receivableRepository.save(receivableEntity);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setReceivableDto(ReceivableMapper.entityToDto(receivableCreated));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Fiado registrado exitosamente");
        return messageResponse;
    }

    @Override
    @Transactional
    public MessageResponse createFromSale(Integer saleId, Integer clientId, BigDecimal pendingBalance, UserEntity user) {
        // No se atrapa la excepción: si el cliente no existe, debe propagar y hacer rollback
        // de toda la venta (SaleServiceImpl.createSale no revisa el MessageResponse acá).
        ClientEntity client = clientRepository.findByClientIdAndCompanyId(clientId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El cliente no existe en su cartera"));

        ReceivableEntity receivableEntity = new ReceivableEntity();
        receivableEntity.setClient(client);
        receivableEntity.setSaleId(saleId);
        receivableEntity.setOriginalAmount(pendingBalance);
        receivableEntity.setBalance(pendingBalance);
        receivableEntity.setConcept("Venta #" + saleId);
        receivableEntity.setDueDate(LocalDate.now().plusDays(30));
        receivableEntity.setStatus("PENDIENTE");
        receivableEntity.setCompanyId(user.getCompany().getCompanyId());
        receivableRepository.save(receivableEntity);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(true);
        messageResponse.setMessage("Cuenta por cobrar creada exitosamente");
        return messageResponse;
    }

    @Override
    @Transactional
    public MessageResponse registerPayment(Long receivableId, CreateReceivablePaymentRequest request, UserEntity user) {
        if (request == null || request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto del abono debe ser mayor a 0");
        }

        ReceivableEntity receivableEntity = receivableRepository.findByReceivableIdAndCompanyId(receivableId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("La cuenta por cobrar no existe"));

        if (request.getAmount().compareTo(receivableEntity.getBalance()) > 0) {
            throw new BusinessException("El abono no puede ser mayor al saldo pendiente");
        }

        ReceivablePaymentEntity paymentEntity = new ReceivablePaymentEntity();
        paymentEntity.setReceivable(receivableEntity);
        paymentEntity.setAmount(request.getAmount());
        paymentEntity.setPaymentMethod(request.getPaymentMethod());
        paymentEntity.setCreatedBy(Long.valueOf(user.getUserId()));
        receivablePaymentRepository.save(paymentEntity);

        BigDecimal newBalance = receivableEntity.getBalance().subtract(request.getAmount());
        receivableEntity.setBalance(newBalance);
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            receivableEntity.setBalance(BigDecimal.ZERO);
            receivableEntity.setStatus("PAGADO");
        }
        receivableRepository.save(receivableEntity);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setReceivableDto(ReceivableMapper.entityToDto(receivableEntity));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Abono registrado exitosamente");
        return messageResponse;
    }

    @Override
    public Page<ReceivablePaymentDto> getPaymentsByReceivable(Long receivableId, UserEntity user, int page, int size) {
        receivableRepository.findByReceivableIdAndCompanyId(receivableId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("La cuenta por cobrar no existe"));

        Pageable pageable = PageRequest.of(page, size);
        Page<ReceivablePaymentEntity> payments = receivablePaymentRepository.findByReceivable_ReceivableIdOrderByPaymentDateDesc(receivableId, pageable);
        return payments.map(ReceivablePaymentMapper::entityToDto);
    }

    @Override
    public Page<ReceivablePaymentDto> getPaymentsByCompany(UserEntity user, String searchKey, String paymentMethod, Integer clientId, String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReceivablePaymentDtoInter> payments = receivablePaymentRepository.findByCompanyWithFilters(
                user.getCompany().getCompanyId(), clientId, paymentMethod, searchKey, fromDate, toDate, pageable
        );
        return payments.map(ReceivablePaymentMapper::interToDto);
    }

    @Override
    public BigDecimal getClientBalance(Integer clientId, UserEntity user) {
        return receivableRepository.sumPendingBalanceByClient(clientId, user.getCompany().getCompanyId());
    }

}
