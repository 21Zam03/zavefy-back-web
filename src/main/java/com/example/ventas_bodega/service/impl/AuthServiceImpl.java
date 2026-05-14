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

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
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

        String accessToken = jwtUtil.createToken(userCreated.getEmail(), nombresRoles, nombresPermisos);
        return new SignUpResponse(userCreated.getEmail(), "Client was registered successfully", accessToken, 200);
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            SignInResponse signInResponse = new SignInResponse();
            signInResponse.setStatus(401);
            signInResponse.setMessage("Contraseña invalida");
            return signInResponse;
        }

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

        String accessToken = jwtUtil.createToken(user.getEmail(), nombresRoles, nombresPermisos);

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setCompanyName(user.getCompany().getComertialName());
        signInResponse.setRole(nombresRoles.get(0));
        signInResponse.setEmail(user.getEmail());
        signInResponse.setFirstname(user.getFirstname());
        signInResponse.setLastname(user.getLastname());

        List<ProductEntity> productEntityList = productRepository.findByCompany_Ruc(user.getCompany().getRuc());
        signInResponse.setProducts(ProductMapper.entityListToDtoList(productEntityList));
        signInResponse.setMessage("User logged successfully");
        signInResponse.setToken(accessToken);
        signInResponse.setStatus(200);

        return signInResponse;
    }

    @Override
    public UserLoggedResponse validateSession(String token) {
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        String email = jwtUtil.extractUsername(decodedJWT);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
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
        return userLoggedResponse;

    }
}
