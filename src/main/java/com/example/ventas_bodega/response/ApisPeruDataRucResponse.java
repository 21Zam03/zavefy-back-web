package com.example.ventas_bodega.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApisPeruDataRucResponse {

    private String ruc;
    private String razonSocial;
    private String nombreComercial;
    private String direccion;

}
