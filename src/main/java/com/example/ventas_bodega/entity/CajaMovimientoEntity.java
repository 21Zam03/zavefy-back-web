package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.CajaMovimientoCategoriaEnum;
import com.example.ventas_bodega.enums.CajaMovimientoTipoEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_caja_movimiento")
public class CajaMovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caja", nullable = false)
    private CajaEntity caja;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private CajaMovimientoTipoEnum tipo;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    // Nullable porque los movimientos creados antes de esta funcionalidad no tienen categoría.
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private CajaMovimientoCategoriaEnum categoria;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    public CajaMovimientoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CajaEntity getCaja() {
        return caja;
    }

    public void setCaja(CajaEntity caja) {
        this.caja = caja;
    }

    public CajaMovimientoTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(CajaMovimientoTipoEnum tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public CajaMovimientoCategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CajaMovimientoCategoriaEnum categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

}
