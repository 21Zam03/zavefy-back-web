package com.example.ventas_bodega.dto;

public class ReniecDataDto {

    private String numeroDNI;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;

    public ReniecDataDto() {}

    public ReniecDataDto(String numeroDNI, String apellidoPaterno, String apellidoMaterno, String nombres) {
        this.numeroDNI = numeroDNI;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombres = nombres;
    }

    public String getNumeroDNI() {
        return numeroDNI;
    }

    public void setNumeroDNI(String numeroDNI) {
        this.numeroDNI = numeroDNI;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
