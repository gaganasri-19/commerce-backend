package com.product.commerce.dto;

import java.time.Instant;
import java.util.List;

import com.product.commerce.entity.OrderItem;
import com.product.commerce.entity.OrderStatus;

public class CreateOrderResponse {
    private Long orderId;
    private String userEmail;
    private List<OrderItem> items;
    private double subtotal;
    private OrderStatus status;
    private Instant createdAt;

    public CreateOrderResponse(Long orderId, String userEmail, List<OrderItem> items, double subtotal, OrderStatus status, Instant createdAt) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.items = items;
        this.subtotal = subtotal;
        this.status = status;
        this.createdAt = createdAt; 
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

