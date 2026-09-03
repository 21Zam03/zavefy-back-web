package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.RoleDto;
import com.example.ventas_bodega.dto.TeamUserDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateUserRequest;
import com.example.ventas_bodega.request.UpdateUserRequest;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<TeamUserDto> getUsersByCompany(UserEntity currentUser, String searchKey, int page, int size);
    List<RoleDto> getAssignableRoles();
    MessageResponse createUser(CreateUserRequest request, UserEntity currentUser);
    MessageResponse updateUser(UpdateUserRequest request, UserEntity currentUser);
    MessageResponse activateUser(Integer userId, UserEntity currentUser);
    MessageResponse deactivateUser(Integer userId, UserEntity currentUser);

}
