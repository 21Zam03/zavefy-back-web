package com.example.ventas_bodega.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.exceptions.DuplicateException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.ClientMapper;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.*;
import com.example.ventas_bodega.request.SignInRequest;
import com.example.ventas_bodega.request.SignUpRequest;
import com.example.ventas_bodega.request.UpdatePasswordRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.response.SignInResponse;
import com.example.ventas_bodega.response.SignUpResponse;
import com.example.ventas_bodega.response.UserLoggedResponse;
import com.example.ventas_bodega.service.AuthService;
import com.example.ventas_bodega.util.JwtUtil;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;


    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            ProductRepository productRepository,
            AuthenticationManager authenticationManager, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateException
                    ("Email "+ signUpRequest.getEmail() +
                            " is already registered in the system");
        }

        Set<RoleEntity> roleList = new HashSet<>();
        signUpRequest.getRoleList().forEach(roleId -> {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(() ->
                    new NotFoundException("Role with id "+roleId+" was not fond" ));
            roleList.add(roleEntity);
        });

        CompanyEntity company = companyRepository.findById(signUpRequest.getIdCompany()).orElseThrow(() -> {
            return new ServiceException("Empresa no existe");
        });

        UserEntity userToCreate = new UserEntity();
        userToCreate.setEmail(signUpRequest.getEmail());
        userToCreate.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userToCreate.setEnabled(true);
        userToCreate.setAccountExpired(false);
        userToCreate.setAccountLocked(false);
        userToCreate.setCredentialExpired(false);
        userToCreate.setRoleList(roleList);
        userToCreate.setCompany(company);
        userToCreate.setUsername(signUpRequest.getUsername());

        UserEntity userCreated = userRepository.save(userToCreate);

        Set<RoleEntity> roles = userCreated.getRoleList();
        Set<PermissionEntity> permisos = userCreated.getRoleList().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .collect(Collectors.toSet());

        List<String> nombresRoles = roles.stream()
                .map(RoleEntity::getName)
                .toList();

        List<String> nombresPermisos = permisos.stream()
                .map(PermissionEntity::getName)
                .toList();

        String accessToken = jwtUtil.createToken(userCreated.getUsername(), nombresRoles, nombresPermisos);
        return new SignUpResponse(userCreated.getEmail(), "Client was registered successfully", accessToken, 200, userCreated.getUsername());
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(
                    "Correo o contraseña incorrectos"
            );
        }

        UserEntity user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() ->
                        new NotFoundException("Usuario no encontrado"));

        Set<RoleEntity> roles = user.getRoleList();

        Set<PermissionEntity> permisos = user.getRoleList().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .collect(Collectors.toSet());

        List<String> nombresRoles = roles.stream()
                .map(RoleEntity::getName)
                .toList();

        List<String> nombresPermisos = permisos.stream()
                .map(PermissionEntity::getName)
                .toList();

        String accessToken = jwtUtil.createToken(
                user.getUsername(),
                nombresRoles,
                nombresPermisos
        );

        SignInResponse signInResponse = new SignInResponse();

        signInResponse.setCompanyName(
                user.getCompany().getComertialName()
        );

        signInResponse.setRole(nombresRoles.get(0));
        signInResponse.setEmail(user.getEmail());
        signInResponse.setUsername(user.getUsername());
        signInResponse.setFirstname(user.getFirstname());
        signInResponse.setLastname(user.getLastname());
        signInResponse.setPasswordReset(user.isPasswordReset());

        List<ProductEntity> productEntityList = productRepository.findByCompany_Ruc(user.getCompany().getRuc());
        List<ClientEntity> clientEntityList = clientRepository.findByCompanyId(user.getCompany().getCompanyId());

        signInResponse.setProducts(ProductMapper.entityListToDtoList(productEntityList));
        signInResponse.setClients(ClientMapper.entityListToDtoList(clientEntityList));
        signInResponse.setMessage("User logged successfully");
        signInResponse.setToken(accessToken);
        signInResponse.setStatus(200);

        return signInResponse;
    }

    @Override
    public UserLoggedResponse validateSession(String token) {
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        String username = jwtUtil.extractUsername(decodedJWT);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        List<RoleEntity> roles = new ArrayList<>(user.getRoleList());

        UserLoggedResponse userLoggedResponse = new UserLoggedResponse();
        userLoggedResponse.setEmail(user.getEmail());
        userLoggedResponse.setType(roles.get(0).getName());
        userLoggedResponse.setIdCompany(user.getCompany().getCompanyId());
        userLoggedResponse.setFirstName(user.getFirstname());
        userLoggedResponse.setLastName(user.getLastname());
        userLoggedResponse.setRuc(user.getCompany().getRuc());
        userLoggedResponse.setComertialName(user.getCompany().getComertialName());
        userLoggedResponse.setSocialReason(user.getCompany().getSocialReason());
        userLoggedResponse.setHasPrinter(user.getCompany().isHasPrinter());
        userLoggedResponse.setHasBarcode(user.getCompany().isHasBarcode());
        userLoggedResponse.setHasStock(user.getCompany().isHasStock());
        userLoggedResponse.setHasAutomaticSaved(user.getCompany().isHasAutomaticSaved());
        userLoggedResponse.setUsername(user.getUsername());
        userLoggedResponse.setPasswordUpdateDate(user.getPasswordUpdateDate());
        userLoggedResponse.setPasswordReset(user.isPasswordReset());
        List<String> roleNames = roles.stream()
                .map(RoleEntity::getName)
                .toList();

        userLoggedResponse.setRoles(roleNames);
        List<String> permissions = roles.stream()
                .flatMap(role -> role.getPermissionList().stream())
                .map(PermissionEntity::getName)
                .distinct()
                .toList();
        userLoggedResponse.setPermissions(permissions);
        return userLoggedResponse;
    }

    @Override
    public MessageResponse updatePassword(UpdatePasswordRequest request, UserEntity user) {
        // 1. Validar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {

            return new MessageResponse(
                    "La contraseña actual es incorrecta",
                    false
            );
        }

        // 2. Validar nueva contraseña
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            return new MessageResponse(
                    "Las nuevas contraseñas no coinciden",
                    false
            );
        }

        // 3. Validar que no sea igual a la anterior
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {

            return new MessageResponse(
                    "La nueva contraseña debe ser diferente a la actual",
                    false
            );
        }

        // 4. Encriptar nueva contraseña
        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        // 5. Actualizar
        int result = userRepository.updatePassword(
                user.getUserId(),
                encodedPassword,
                user.getUserId().longValue()
        );

        if (result == 1) {

            return new MessageResponse(
                    "Contraseña actualizada correctamente",
                    true
            );
        }

        return new MessageResponse(
                "No se pudo actualizar la contraseña",
                false
        );
    }
}
