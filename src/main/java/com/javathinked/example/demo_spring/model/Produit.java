package com.javathinked.example.demo_spring.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    private Float price;

    @Column(length = 255)
    private String description;

    private Integer stock;

    @Column(length = 255) // ta table actuelle est en 255 ; OK même si le DTO limite à 50
    private String color;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "created_at") // mapping explicite vers la colonne existante
    private LocalDateTime createdAt;

    // ====== Constructeurs ======
    public Produit() {}

    public Produit(String name, Float price, String description, Integer stock, String color, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    // ====== Hooks JPA ======
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ====== Getters / Setters ======
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
