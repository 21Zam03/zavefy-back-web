package com.example.ventas_bodega.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReniecDataResponse {

    private String numeroDNI;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;

    public ReniecDataResponse() {}

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
