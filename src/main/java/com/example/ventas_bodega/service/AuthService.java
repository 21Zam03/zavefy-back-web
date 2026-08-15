package com.example.ventas_bodega.service;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.SignInRequest;
import com.example.ventas_bodega.request.SignUpRequest;
import com.example.ventas_bodega.request.UpdatePasswordRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.response.SignInResponse;
import com.example.ventas_bodega.response.SignUpResponse;
import com.example.ventas_bodega.response.UserLoggedResponse;

public interface AuthService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);
    public SignInResponse signIn(SignInRequest signInRequest);
    public UserLoggedResponse validateSession(String token);
    MessageResponse updatePassword(UpdatePasswordRequest request, UserEntity user);

}
