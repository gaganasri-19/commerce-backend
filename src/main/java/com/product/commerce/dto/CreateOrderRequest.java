package com.product.commerce.dto;

import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {
    @NotNull
    private Long productId;
    
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
