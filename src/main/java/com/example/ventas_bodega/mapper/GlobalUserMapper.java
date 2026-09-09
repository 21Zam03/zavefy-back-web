package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.GlobalUserDto;
import com.example.ventas_bodega.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalUserMapper {

    public static GlobalUserDto entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        GlobalUserDto dto = new GlobalUserDto();
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstname());
        dto.setLastName(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setEnabled(entity.isEnabled());
        dto.setRoles(
                entity.getRoleList() == null
                        ? Collections.emptyList()
                        : RoleMapper.entityListToDtoList(new ArrayList<>(entity.getRoleList()))
        );
        if (entity.getCompany() != null) {
            dto.setCompanyId(entity.getCompany().getCompanyId());
            dto.setCompanyName(entity.getCompany().getComertialName());
            dto.setCompanyRuc(entity.getCompany().getRuc());
        }
        return dto;
    }

}
