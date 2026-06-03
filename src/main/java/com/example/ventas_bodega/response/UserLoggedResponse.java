package com.example.ventas_bodega.response;

public class UserLoggedResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Long idCompany;
    private boolean hasPrinter;
    private boolean hasStock;
    private boolean hasBarcode;
    private boolean hasAutomaticSaved;
    private String role;
    private String type;
    private String ruc;
    private String socialReason;
    private String comertialName;

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
}
