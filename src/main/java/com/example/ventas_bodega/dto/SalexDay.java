package com.example.ventas_bodega.dto;

import java.math.BigDecimal;

public class SalexDay {

    private String fecha;
    private BigDecimal total;

    public SalexDay() {}

    public SalexDay(String fecha, BigDecimal total) {
        this.fecha = fecha;
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
