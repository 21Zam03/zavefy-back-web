package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_venta_pago")
public class SalePaymentLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta_pago")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private SaleEntity saleEntity;

    @Column(name = "metodo")
    private String method;

    @Column(name = "monto")
    private BigDecimal amount;

    @Column(name = "comision_porcentaje")
    private BigDecimal cardCommissionPercent;

    @Column(name = "comision_monto")
    private BigDecimal cardCommissionAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleEntity getSaleEntity() {
        return saleEntity;
    }

    public void setSaleEntity(SaleEntity saleEntity) {
        this.saleEntity = saleEntity;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCardCommissionPercent() {
        return cardCommissionPercent;
    }

    public void setCardCommissionPercent(BigDecimal cardCommissionPercent) {
        this.cardCommissionPercent = cardCommissionPercent;
    }

    public BigDecimal getCardCommissionAmount() {
        return cardCommissionAmount;
    }

    public void setCardCommissionAmount(BigDecimal cardCommissionAmount) {
        this.cardCommissionAmount = cardCommissionAmount;
    }
}
