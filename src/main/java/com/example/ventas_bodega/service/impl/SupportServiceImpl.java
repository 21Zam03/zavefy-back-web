package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.entity.SupportMessageEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.repository.SupportMessageRepository;
import com.example.ventas_bodega.request.CreateSupportMessageRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl implements SupportService {

    private final SupportMessageRepository supportMessageRepository;

    @Autowired
    public SupportServiceImpl(SupportMessageRepository supportMessageRepository) {
        this.supportMessageRepository = supportMessageRepository;
    }

    @Override
    public MessageResponse createMessage(CreateSupportMessageRequest request, UserEntity user) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            throw new BusinessException("Escribe un mensaje antes de enviarlo");
        }

        SupportMessageEntity entity = new SupportMessageEntity();
        entity.setSubject(request.getSubject());
        entity.setMessage(request.getMessage());
        entity.setContactEmail(
                request.getContactEmail() != null && !request.getContactEmail().isBlank()
                        ? request.getContactEmail()
                        : user.getEmail()
        );
        entity.setUserId(Long.valueOf(user.getUserId()));
        entity.setCompanyId(user.getCompany() != null ? user.getCompany().getCompanyId() : null);

        supportMessageRepository.save(entity);

        return new MessageResponse("Tu mensaje fue enviado, te contactaremos pronto", true);
    }

}
