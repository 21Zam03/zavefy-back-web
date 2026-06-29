package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_carrito")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private UserEntity customer;
    private String customerPhoneNumber;
    private String customerName;
    private String subject;
    private String status;
    private LocalDateTime createdAt;

}
