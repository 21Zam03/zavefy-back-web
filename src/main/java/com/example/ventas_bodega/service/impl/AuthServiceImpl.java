package com.example.ventas_bodega.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ventas_bodega.entity.*;
import com.example.ventas_bodega.exceptions.DuplicateException;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.RoleRepository;
import com.example.ventas_bodega.repository.UserRepository;
import com.example.ventas_bodega.request.SignInRequest;
import com.example.ventas_bodega.request.SignUpRequest;
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


    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            ProductRepository productRepository,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.authenticationManager = authenticationManager;
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

        List<ProductEntity> productEntityList =
                productRepository.findByCompany_Ruc(
                        user.getCompany().getRuc()
                );

        signInResponse.setProducts(
                ProductMapper.entityListToDtoList(productEntityList)
        );

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
        List<RoleEntity> list = new ArrayList<>(user.getRoleList());

        UserLoggedResponse userLoggedResponse = new UserLoggedResponse();
        userLoggedResponse.setEmail(user.getEmail());
        userLoggedResponse.setType(list.get(0).getName());
        userLoggedResponse.setIdCompany(user.getCompany().getCompanyId());
        userLoggedResponse.setFirstName(user.getFirstname());
        userLoggedResponse.setLastName(user.getLastname());
        userLoggedResponse.setRuc(user.getCompany().getRuc());
        userLoggedResponse.setComertialName(user.getCompany().getComertialName());
        userLoggedResponse.setSocialReason(user.getCompany().getSocialReason());
        userLoggedResponse.setHasBarcode(user.getCompany().isHasBarcode());
        userLoggedResponse.setHasStock(user.getCompany().isHasStock());
        userLoggedResponse.setHasAutomaticSaved(user.getCompany().isHasAutomaticSaved());
        userLoggedResponse.setUsername(user.getUsername());
        return userLoggedResponse;

    }
}
