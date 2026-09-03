package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.RoleDto;
import com.example.ventas_bodega.dto.TeamUserDto;
import com.example.ventas_bodega.entity.RoleEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.BusinessException;
import com.example.ventas_bodega.exceptions.DuplicateException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.RoleMapper;
import com.example.ventas_bodega.mapper.TeamUserMapper;
import com.example.ventas_bodega.repository.RoleRepository;
import com.example.ventas_bodega.repository.UserRepository;
import com.example.ventas_bodega.request.CreateUserRequest;
import com.example.ventas_bodega.request.UpdateUserRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<TeamUserDto> getUsersByCompany(UserEntity currentUser, String searchKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> users = userRepository.findByCompanyWithFilters(
                currentUser.getCompany().getCompanyId(), searchKey, pageable);
        return users.map(TeamUserMapper::entityToDto);
    }

    @Override
    public List<RoleDto> getAssignableRoles() {
        return roleRepository.findAll().stream()
                .filter(role -> !Boolean.TRUE.equals(role.getIsSystemRole()))
                .map(RoleMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse createUser(CreateUserRequest request, UserEntity currentUser) {
        if (request == null
                || request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getLastName() == null || request.getLastName().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("Nombres, apellidos y correo son obligatorios");
        }
        if (request.getDocumentNumber() == null || !request.getDocumentNumber().matches("\\d{8}")) {
            throw new BusinessException("Ingresa un DNI válido de 8 dígitos: se usará como contraseña inicial");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("El correo " + request.getEmail() + " ya está registrado");
        }

        Set<RoleEntity> roles = request.getRoleIds() == null
                ? new HashSet<>()
                : roleRepository.findAllById(request.getRoleIds()).stream()
                    .filter(role -> !Boolean.TRUE.equals(role.getIsSystemRole()))
                    .collect(Collectors.toSet());
        if (roles.isEmpty()) {
            throw new BusinessException("Selecciona al menos un rol para el usuario");
        }

        // Contraseña inicial = DNI del usuario (pedido explícito); passwordReset=true lo obliga a cambiarla al ingresar.
        UserEntity userToCreate = new UserEntity();
        userToCreate.setFirstname(request.getFirstName());
        userToCreate.setLastname(request.getLastName());
        userToCreate.setEmail(request.getEmail());
        userToCreate.setUsername(request.getEmail());
        userToCreate.setPassword(passwordEncoder.encode(request.getDocumentNumber()));
        userToCreate.setEnabled(true);
        userToCreate.setAccountExpired(false);
        userToCreate.setAccountLocked(false);
        userToCreate.setCredentialExpired(false);
        userToCreate.setPasswordReset(true);
        userToCreate.setPasswordUpdateDate(LocalDateTime.now());
        userToCreate.setRoleList(roles);
        userToCreate.setCompany(currentUser.getCompany());
        userToCreate.setCreatedBy(Long.valueOf(currentUser.getUserId()));

        userRepository.save(userToCreate);

        return new MessageResponse("Usuario creado. Su contraseña inicial es su DNI; deberá cambiarla al ingresar.", true);
    }

    @Override
    @Transactional
    public MessageResponse updateUser(UpdateUserRequest request, UserEntity currentUser) {
        if (request == null || request.getUserId() == null) {
            throw new BusinessException("Falta el usuario a actualizar");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getLastName() == null || request.getLastName().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("Nombre, apellido y correo son obligatorios");
        }

        UserEntity target = userRepository
                .findByUserIdAndCompany_CompanyId(request.getUserId(), currentUser.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        target.setFirstname(request.getFirstName());
        target.setLastname(request.getLastName());
        target.setEmail(request.getEmail());

        if (request.getRoleIds() != null) {
            List<RoleEntity> assignableRoles = roleRepository.findAllById(request.getRoleIds()).stream()
                    .filter(role -> !Boolean.TRUE.equals(role.getIsSystemRole()))
                    .collect(Collectors.toList());
            Set<RoleEntity> roles = new HashSet<>(assignableRoles);
            target.setRoleList(roles);
        }

        target.setUpdatedBy(Long.valueOf(currentUser.getUserId()));
        userRepository.save(target);

        return new MessageResponse("Usuario actualizado correctamente", true);
    }

    @Override
    public MessageResponse activateUser(Integer userId, UserEntity currentUser) {
        UserEntity target = userRepository
                .findByUserIdAndCompany_CompanyId(userId, currentUser.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));
        target.setEnabled(true);
        userRepository.save(target);
        return new MessageResponse("Usuario activado", true);
    }

    @Override
    public MessageResponse deactivateUser(Integer userId, UserEntity currentUser) {
        if (userId.equals(currentUser.getUserId())) {
            throw new BusinessException("No puedes desactivarte a ti mismo");
        }
        UserEntity target = userRepository
                .findByUserIdAndCompany_CompanyId(userId, currentUser.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));
        target.setEnabled(false);
        userRepository.save(target);
        return new MessageResponse("Usuario desactivado", true);
    }

}
