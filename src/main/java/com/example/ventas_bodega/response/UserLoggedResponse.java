package com.example.ventas_bodega.response;

import java.time.LocalDateTime;
import java.util.List;

public class UserLoggedResponse {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Long idCompany;
    private boolean hasPrinter;
    private boolean hasStock;
    private boolean hasBarcode;
    private boolean hasAutomaticSaved;
    private List<String> roles;
    private List<String> permissions;
    private String role;
    private String type;
    private String ruc;
    private String socialReason;
    private String comertialName;
    private LocalDateTime passwordUpdateDate;
    private boolean passwordReset;

    public UserLoggedResponse() {
    }

    public UserLoggedResponse(String firstName, String lastName, String email, Long idCompany, String role, String type, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idCompany = idCompany;
        this.role = role;
        this.type = type;
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getComertialName() {
        return comertialName;
    }

    public void setComertialName(String comertialName) {
        this.comertialName = comertialName;
    }

    public boolean isHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public boolean isHasBarcode() {
        return hasBarcode;
    }

    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }

    public boolean isHasAutomaticSaved() {
        return hasAutomaticSaved;
    }

    public void setHasAutomaticSaved(boolean hasAutomaticSaved) {
        this.hasAutomaticSaved = hasAutomaticSaved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHasPrinter() {
        return hasPrinter;
    }

    public void setHasPrinter(boolean hasPrinter) {
        this.hasPrinter = hasPrinter;
    }

    public LocalDateTime getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    public void setPasswordUpdateDate(LocalDateTime passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
