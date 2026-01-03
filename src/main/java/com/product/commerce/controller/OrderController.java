package com.product.commerce.controller;

import com.product.commerce.dto.CreateOrderRequest;
import com.product.commerce.entity.Order;
import com.product.commerce.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(@AuthenticationPrincipal UserDetails user,
                            @Valid @RequestBody CreateOrderRequest request) {

        return orderService.createOrder(
                user.getUsername(), // email
                request.getProductId(),
                request.getQuantity()
        );
    }
}
