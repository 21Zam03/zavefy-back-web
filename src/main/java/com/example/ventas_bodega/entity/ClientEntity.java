package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cliente")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer clientId;

    @Column(name = "nombres")
    private String firstname;

    @Column(name = "apellidos")
    private String lastname;

    @Column(name = "correo", unique = true, length = 50, nullable = false)
    private String email;

    @Column(name= "activo")
    private boolean isEnabled;

    @Column(name = "id_empresa")
    private Long companyId;

    @Column(name = "numero_documento")
    private String documentNumber;

    @Column(name = "tipo_documento")
    private String clientDocumentType;

    @Column(name = "numero_telefono")
    private String phoneNumber;

    @Column(name = "direccion")
    private String address;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdDate;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }



}
