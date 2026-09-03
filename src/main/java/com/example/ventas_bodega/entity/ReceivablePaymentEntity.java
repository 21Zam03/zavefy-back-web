package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_abono")
public class ReceivablePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abono")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "id_cuenta_por_cobrar")
    private ReceivableEntity receivable;

    @Column(name = "monto")
    private BigDecimal amount;

    @Column(name = "metodo_pago")
    private String paymentMethod;

    @Column(name = "fecha_pago")
    private LocalDateTime paymentDate;

    @Column(name = "creado_por")
    private Long createdBy;

    @PrePersist
    public void prePersist() {
        paymentDate = LocalDateTime.now();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public ReceivableEntity getReceivable() {
        return receivable;
    }

    public void setReceivable(ReceivableEntity receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
