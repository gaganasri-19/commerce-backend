package com.product.commerce.controller;

import com.product.commerce.dto.CreateOrderRequest;
import com.product.commerce.dto.CreateOrderResponse;
import com.product.commerce.entity.Order;
import com.product.commerce.service.OrderService;
import com.product.commerce.service.RateLimiterService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final RateLimiterService rateLimiterService;

    public OrderController(OrderService orderService, RateLimiterService rateLimiterService) {
        this.orderService = orderService;
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping
    public List<Order> getUserOrders(@AuthenticationPrincipal UserDetails user) {
        return orderService.getUserOrders(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<Object> placeOrder(@AuthenticationPrincipal UserDetails user,
                            @Valid @RequestBody CreateOrderRequest request) {

    if (!rateLimiterService.isAllowed(user.getUsername())) {
        return ResponseEntity
                .status(429)
                .body("Rate limit exceeded. Max 5 orders per minute allowed.");
    }
    CreateOrderResponse response = orderService.createOrder(user.getUsername(), request);

    return ResponseEntity
                .status(201)
                .body(response);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

}
