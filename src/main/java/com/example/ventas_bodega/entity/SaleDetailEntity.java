package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_venta")
public class SaleDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private SaleEntity saleEntity;

    @Column(name = "cantidad", precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "precio_unitario")
    private BigDecimal unitePrice;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "id_producto")
    private Long productId;

    @Column(name = "unidad_medida")
    private String measurementUnit;

    @Column(name = "notas")
    private String notes;

    @Column(name = "nombre_producto")
    private String name;

    public SaleDetailEntity() {}

    public SaleDetailEntity(Long id, SaleEntity saleEntity, BigDecimal quantity, BigDecimal unitePrice, BigDecimal total) {
        this.id = id;
        this.saleEntity = saleEntity;
        this.quantity = quantity;
        this.unitePrice = unitePrice;
        this.total = total;
    }

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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(BigDecimal unitePrice) {
        this.unitePrice = unitePrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

}
