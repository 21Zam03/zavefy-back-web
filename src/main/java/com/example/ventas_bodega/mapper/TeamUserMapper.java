package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.TeamUserDto;
import com.example.ventas_bodega.entity.PermissionEntity;
import com.example.ventas_bodega.entity.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeamUserMapper {

    public static TeamUserDto entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        TeamUserDto dto = new TeamUserDto();
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstname());
        dto.setLastName(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setEnabled(entity.isEnabled());

        if (entity.getRoleList() == null) {
            dto.setRoles(Collections.emptyList());
            dto.setPermissions(Collections.emptyList());
            return dto;
        }

        dto.setRoles(RoleMapper.entityListToDtoList(entity.getRoleList().stream().collect(Collectors.toList())));

        List<String> permissions = entity.getRoleList().stream()
                .filter(role -> role.getPermissionList() != null)
                .flatMap(role -> role.getPermissionList().stream())
                .map(PermissionEntity::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        dto.setPermissions(permissions);

        return dto;
    }

}
