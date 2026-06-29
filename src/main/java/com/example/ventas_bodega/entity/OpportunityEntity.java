package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.*;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_oportunidad")
public class OpportunityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @Column(name = "codigo")
    private String code; // OP-000001

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private OpportunityStatusEnum status;

    @Column(name = "prioridad")
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @Column(name = "numero_contacto")
    private String phoneNumber;

    @Column(name = "nombre_completo")
    private String fullName;

    //private ClientEntity clientEntity;

    @ManyToOne
    @JoinColumn(name = "id_usuario_asignado", nullable = false)
    private UserEntity assignedTo;

    @Column(name = "origen")
    @Enumerated(EnumType.STRING)
    private SourceEnum source;

    @Column(name = "asunto")
    @Enumerated(EnumType.STRING)
    private SubjectEnum subject;

    @Column(name = "mensaje")
    private String message;

    @Column(name = "resultado")
    private ResultEnum result;

    private String lostReason;

    private String internalNotes;


}
