package com.example.ventas_bodega.response;

public class SignUpResponse {

    private String username;
    private String email;
    private String message;
    private String token;
    private Integer status;

    public SignUpResponse() {
    }

    public SignUpResponse(String email, String message, String token, Integer status, String username) {
        this.email = email;
        this.message = message;
        this.token = token;
        this.status = status;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

