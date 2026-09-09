package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.GlobalCompanyDto;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.SubscriptionEntity;

public class GlobalCompanyMapper {

    // usersCount y subscription se resuelven aparte (una sola query agrupada para toda la
    // página cada una) y se pasan aquí para no disparar una query por empresa.
    public static GlobalCompanyDto entityToDto(CompanyEntity entity, long usersCount, SubscriptionEntity subscription) {
        if (entity == null) {
            return null;
        }
        GlobalCompanyDto dto = new GlobalCompanyDto();
        dto.setCompanyId(entity.getCompanyId());
        dto.setRuc(entity.getRuc());
        dto.setSocialReason(entity.getSocialReason());
        dto.setComertialName(entity.getComertialName());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setActive(entity.isActive());
        dto.setTest(entity.isTest());
        dto.setHasStock(entity.isHasStock());
        dto.setHasBarcode(entity.isHasBarcode());
        dto.setHasPrinter(entity.isHasPrinter());
        dto.setUsersCount(usersCount);
        if (subscription != null) {
            dto.setPlanName(subscription.getPlan().getName());
            dto.setSubscriptionStatus(subscription.getStatus().name());
        }
        return dto;
    }

}
