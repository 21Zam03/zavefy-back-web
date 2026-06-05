package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_modulo")
public class ModuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo")
    private Integer moduleId;

    @Column(name = "id_modulo_padre")
    private Integer fatherModuleId;

    @Column(name = "nombre")
    private String name;

    @Column(name = "codigo")
    private String code;

    @Column(name = "icono")
    private String icon;

    @Column(name = "orden")
    private Integer order;

    @Column(name = "es_activo")
    private boolean isActive;

}
