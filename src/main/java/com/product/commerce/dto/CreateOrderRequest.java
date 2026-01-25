package com.product.commerce.dto;

import java.util.List;

public class CreateOrderRequest {

    private List<OrderItemRequest> items;

    public static class OrderItemRequest {
        private Long productId;
        private int quantity;

        public Long getProductId() {
            return productId;
        }
        public int getQuantity() {
            return quantity;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }       
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

}
