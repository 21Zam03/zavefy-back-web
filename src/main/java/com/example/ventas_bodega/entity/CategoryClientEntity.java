package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

@Table(name = "tb_categoria_cliente")
@Entity
public class CategoryClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_categoria", nullable = false)
    private Long categoryId;

    @Column(name = "id_cliente", nullable = false)
    private Long clientId;

    public CategoryClientEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
