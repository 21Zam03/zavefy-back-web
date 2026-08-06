package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.OpportunityDto;
import com.example.ventas_bodega.dto.interfaces.ClientDtoInter;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.enums.*;
import com.example.ventas_bodega.mapper.ClientMapper;
import com.example.ventas_bodega.mapper.OpportunityMapper;
import com.example.ventas_bodega.repository.CartRepository;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.OpportunityRepository;
import com.example.ventas_bodega.service.OpportunityService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final CartRepository cartRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, CartRepository cartRepository, CompanyRepository companyRepository) {
        this.opportunityRepository = opportunityRepository;
        this.cartRepository = cartRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public void createOpportunity() {
        List<CartEntity> cartList = cartRepository.findByStatus("PENDING");

        if(cartList.isEmpty()) {
            System.out.println("NO HAY OPORTUNIDADES PENDIENTES QUE PROCESAR");
            return;
        }

        List<OpportunityEntity> batch = new ArrayList<>();

        for (CartEntity cart : cartList) {

            OpportunityEntity opportunity = new OpportunityEntity();

            Integer lastNumber = opportunityRepository.findLastOpportunityNumber(cart.getCompanyEntity().getUsers().get(0).getUserId());
            if(lastNumber == null) {
                lastNumber = 0;
            }
            lastNumber++;
            // Mapear campos
            opportunity.setCode("OP-"+lastNumber);
            opportunity.setStatus(OpportunityStatusEnum.NEW);

            //La prioridad queda pendiente en validar bajo que condicion se aplicara.
            opportunity.setPriority(PriorityEnum.URGENT);
            opportunity.setPhoneNumber(cart.getCustomerPhoneNumber());
            opportunity.setFullName(cart.getCustomerName());

            //A largo plazo pensar bajo que condicion se asignara a los usuarios quienes atenderan las oportunidades
            UserEntity userEntity = cart.getCompanyEntity().getUsers().get(0);
            opportunity.setAssignedTo(userEntity);

            //A largo pensar en otras opciones de fuente, ya sea en redes sociales o otra.
            opportunity.setSource(SourceEnum.CATALOG);

            //Esto se debera obtener desde el carrito, por ahora solo esta para informacion de producto
            opportunity.setSubject(SubjectEnum.PRODUCT_INFORMATION);

            //Pensar en como obtener el mensaje desde el carrito de compras
            opportunity.setMessage("");

            opportunity.setResult(ResultEnum.OPEN);

            List<OpportunityDetailEntity> details = new ArrayList<>();
            for (CartDetailEntity cartDetail : cart.getCartDetails()) {

                OpportunityDetailEntity detail = new OpportunityDetailEntity();

                detail.setOpportunityEntity(opportunity); // MUY IMPORTANTE

                detail.setProductId(cartDetail.getProductId());
                detail.setQuantity(cartDetail.getQuantity());
                detail.setPriceAtMoment(cartDetail.getPrice());

                details.add(detail);
            }
            opportunity.setDetails(details);

            batch.add(opportunity);

            cart.setStatus("PROCESSED");

        }

        opportunityRepository.saveAll(batch);
        cartRepository.saveAll(cartList);
    }

    @Override
    public Page<OpportunityDto> getOpportunitiesByCompany(UserEntity user, String status, String priority, String fromDate, String toDate, String source, String subject, String result, int page, int size) throws BadRequestException {
        Pageable pageable = PageRequest.of(page, size);

        OpportunityStatusEnum statusEnum = parseEnum(OpportunityStatusEnum.class, status);
        PriorityEnum priorityEnum = parseEnum(PriorityEnum.class, priority);
        SourceEnum sourceEnum = parseEnum(SourceEnum.class, source);
        SubjectEnum subjectEnum = parseEnum(SubjectEnum.class, subject);
        ResultEnum resultEnum = parseEnum(ResultEnum.class, result);

        LocalDateTime fromDateTime = fromDate != null && !fromDate.isBlank()
                ? LocalDate.parse(fromDate).atStartOfDay()
                : null;

        LocalDateTime toDateTime = toDate != null && !toDate.isBlank()
                ? LocalDate.parse(toDate).atTime(LocalTime.MAX)
                : null;

        Page<OpportunityEntity> opportunityEntities = opportunityRepository.findOpportunities(user.getUserId(), statusEnum, priorityEnum, sourceEnum, subjectEnum, resultEnum, fromDateTime, toDateTime, pageable);
        List<OpportunityDto> data = new ArrayList<>();
        for (int i = 0; i<opportunityEntities.getContent().size(); i++) {
            data.add(OpportunityMapper.entityToDto(opportunityEntities.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, opportunityEntities.getTotalElements());
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) throws BadRequestException {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor inválido: " + value);
        }
    }

}
