package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.OpportunityDto;
import com.example.ventas_bodega.entity.UserEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface OpportunityService {

    public void createOpportunity();
    public Page<OpportunityDto> getOpportunitiesByCompany(UserEntity user, String status, String priority, String fromDate, String toDate, String source, String subject, String result, int page, int size) throws BadRequestException;

}
