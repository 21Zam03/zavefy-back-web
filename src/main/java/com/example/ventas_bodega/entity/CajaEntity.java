package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_caja")
public class CajaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caja")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity user;

    @Column(name = "monto_inicial", nullable = false)
    private BigDecimal montoInicial;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "monto_contado")
    private BigDecimal montoContado;

    @Column(name = "monto_esperado")
    private BigDecimal montoEsperado;

    @Column(name = "diferencia")
    private BigDecimal diferencia;

    @Column(name = "observacion_cierre")
    private String observacionCierre;

    @PrePersist
    public void prePersist() {
        this.fechaApertura = LocalDateTime.now();
    }

    public CajaEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(BigDecimal montoInicial) {
        this.montoInicial = montoInicial;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getMontoContado() {
        return montoContado;
    }

    public void setMontoContado(BigDecimal montoContado) {
        this.montoContado = montoContado;
    }

    public BigDecimal getMontoEsperado() {
        return montoEsperado;
    }

    public void setMontoEsperado(BigDecimal montoEsperado) {
        this.montoEsperado = montoEsperado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public String getObservacionCierre() {
        return observacionCierre;
    }

    public void setObservacionCierre(String observacionCierre) {
        this.observacionCierre = observacionCierre;
    }

}
