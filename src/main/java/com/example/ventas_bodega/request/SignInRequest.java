package com.example.ventas_bodega.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class SignInRequest {

    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be an email address with correct format")
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    public SignInRequest() {
    }

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
