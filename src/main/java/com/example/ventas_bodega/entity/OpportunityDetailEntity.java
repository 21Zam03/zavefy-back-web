package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_oportunidad")
public class OpportunityDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_oportunidad")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_oportunidad")
    private OpportunityEntity opportunityEntity;

    @Column(name = "id_producto")
    private Long productId;

    @Column(name = "cantidad")
    private Integer quantity;

    @Column(name = "precio_en_momento")
    private BigDecimal priceAtMoment;

}
