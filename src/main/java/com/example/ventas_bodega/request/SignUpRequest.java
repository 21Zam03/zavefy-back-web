package com.example.ventas_bodega.request;

import java.util.Set;

public class SignUpRequest {

    private String username;
    private String email;
    private String password;
    private Set<Integer> roleList;
    private Long idCompany;

    public SignUpRequest() {
    }

    public SignUpRequest(String email, String password, Set<Integer> roleList, Long idCompany, String username) {
        this.email = email;
        this.password = password;
        this.roleList = roleList;
        this.idCompany = idCompany;
        this.username = username;
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

    public Set<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Integer> roleList) {
        this.roleList = roleList;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
