package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "tb_producto_general",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_producto_general_codigo_barras",
                        columnNames = "codigo_barras"
                )
        }
)
public class ProductGeneralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "codigo_barras")
    private String barcode;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "precio")
    private BigDecimal price;

    @Column(name = "categorias")
    private String categories;

    @Column(name = "imagen_url")
    private String imageUrl;

    @Column(name = "imagen_url_medium")
    private String imageUrlMedium;

    @Column(name = "imagen_url_thumb")
    private String imageUrlThumb;

    @Column(name = "categoria")
    private String category;

    public ProductGeneralEntity() {}

    public ProductGeneralEntity(Long id, String name, String description, BigDecimal price, String categories, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlMedium() {
        return imageUrlMedium;
    }

    public void setImageUrlMedium(String imageUrlMedium) {
        this.imageUrlMedium = imageUrlMedium;
    }

    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    public void setImageUrlThumb(String imageUrlThumb) {
        this.imageUrlThumb = imageUrlThumb;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProductGeneralEntity{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", categories='" + categories + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageUrlMedium='" + imageUrlMedium + '\'' +
                ", imageUrlThumb='" + imageUrlThumb + '\'' +
                '}';
    }
}
