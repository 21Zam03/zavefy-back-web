package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cuenta_por_cobrar")
public class ReceivableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta_por_cobrar")
    private Long receivableId;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientEntity client;

    @Column(name = "id_venta")
    private Integer saleId;

    @Column(name = "monto_original")
    private BigDecimal originalAmount;

    @Column(name = "saldo")
    private BigDecimal balance;

    @Column(name = "concepto")
    private String concept;

    @Column(name = "fecha_limite")
    private LocalDate dueDate;

    @Column(name = "estado")
    private String status;

    @Column(name = "empresa_id")
    private Long companyId;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }

    public Long getReceivableId() {
        return receivableId;
    }

    public void setReceivableId(Long receivableId) {
        this.receivableId = receivableId;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
