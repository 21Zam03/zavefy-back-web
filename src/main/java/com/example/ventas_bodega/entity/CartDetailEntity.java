package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_carrito")
public class CartDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_carrito")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito")
    private CartEntity cartEntity;

    @Column(name = "cantidad", precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "precio")
    private BigDecimal price;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "nombre_producto")
    private String productName;

    @Column(name = "id_producto")
    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity getCartEntity() {
        return cartEntity;
    }

    public void setCartEntity(CartEntity cartEntity) {
        this.cartEntity = cartEntity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
