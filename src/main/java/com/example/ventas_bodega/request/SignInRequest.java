package com.example.ventas_bodega.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class SignInRequest {

    /*
    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be an email address with correct format")
    private String email;
    * */

    @NotNull(message = "Username must not be null")
    private String username;

    @NotNull(message = "Password must not be null")
    private String password;

    public SignInRequest() {
    }

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /*
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    * */

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
