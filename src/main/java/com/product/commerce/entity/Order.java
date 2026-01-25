package com.product.commerce.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    private double subtotal;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Instant createdAt;

    public Order() {}
    
    public Order(User user) {
        this.user = user;
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public double getSubtotal() {   
        return subtotal;
    }                       

    public double setSubtotal() {
         if (items == null || items.isEmpty()) {
        return 0.0; // Return 0 if there are no items
    }
        return  this.subtotal = items.stream()
            .mapToDouble(i -> i.getPriceAtPurchase() * i.getQuantity())
            .sum();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public void markPaid() {
    if (this.status != OrderStatus.CREATED) {
        throw new IllegalStateException("Invalid state transition");
    }
        this.status = OrderStatus.PAID;
    }

    public void markCancel() {
    if (this.status != OrderStatus.CREATED) {
        throw new IllegalStateException("Invalid state transition");
    }
        this.status = OrderStatus.CANCELLED;
    }

    public void addItem(OrderItem item) {
    items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
   }

}
