package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_carrito")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity companyEntity;

    @Column(name = "numero_contacto")
    private String customerPhoneNumber;

    @Column(name = "nombre_completo")
    private String customerName;

    @Column(name = "asunto")
    private String subject;

    @Column(name = "estado")
    private String status;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "cartEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartDetailEntity> cartDetails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public List<CartDetailEntity> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetailEntity> cartDetails) {
        this.cartDetails = cartDetails;
    }
}
