package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notificacion")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long notificationId;

    @Column(name = "mensaje")
    private String message;

    @Column(name = "leido")
    private boolean read;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;

    @Column(name = "id_usuario")
    private Long userId;

}
