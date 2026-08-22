package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.PlanDto;
import com.example.ventas_bodega.entity.PlanEntity;
import com.example.ventas_bodega.mapper.PlanMapper;
import com.example.ventas_bodega.repository.PlanRepository;
import com.example.ventas_bodega.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final PlanRepository planRepository;

    @Override
    public List<PlanDto> getAllPlans() {
        List<PlanEntity> planDtoList = planRepository.findAll();
        if (!planDtoList.isEmpty()) {
            return PlanMapper.entityListToDtoList(planDtoList);
        }
        return List.of();
    }


}
