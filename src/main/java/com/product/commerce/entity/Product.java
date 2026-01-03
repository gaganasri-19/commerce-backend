package com.product.commerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    protected Product() {}

    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = ProductStatus.ACTIVE;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void reduceStock(int quantity) {
    if (this.stock < quantity) {
        throw new IllegalStateException("Stock cannot go negative");
    }
    this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
    if (quantity < 0) {
        throw new IllegalArgumentException("Quantity must be positive");
    }
    this.stock += quantity;
}

}
