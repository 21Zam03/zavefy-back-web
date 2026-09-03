package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.RoleDto;
import com.example.ventas_bodega.entity.PermissionEntity;
import com.example.ventas_bodega.entity.RoleEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDto entityToDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        RoleDto dto = new RoleDto();
        dto.setRoleId(entity.getRoleId());
        dto.setName(entity.getName());
        dto.setSystemRole(Boolean.TRUE.equals(entity.getIsSystemRole()));
        dto.setPermissions(
                entity.getPermissionList() == null
                        ? Collections.emptyList()
                        : entity.getPermissionList().stream()
                            .map(PermissionEntity::getName)
                            .sorted()
                            .collect(Collectors.toList())
        );
        return dto;
    }

    public static List<RoleDto> entityListToDtoList(List<RoleEntity> entities) {
        return entities.stream().map(RoleMapper::entityToDto).collect(Collectors.toList());
    }

}
