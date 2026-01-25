package com.product.commerce.event;

public class OrderCreatedEvent {

    private Long orderId;
    private String userEmail;
    private Double amount;

    public OrderCreatedEvent(Long orderId, String userEmail, Double amount) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Double getAmount() {
        return amount;
    }
}

