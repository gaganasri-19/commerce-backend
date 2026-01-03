package com.product.commerce.controller;

import com.product.commerce.dto.CreateOrderRequest;
import com.product.commerce.entity.Order;
import com.product.commerce.service.OrderService;

import jakarta.validation.Valid;

import java.util.List;

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

    @GetMapping
    public List<Order> getUserOrders(@AuthenticationPrincipal UserDetails user) {
        return orderService.getUserOrders(user.getUsername());
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

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails user) {
    orderService.cancelOrder(orderId, user.getUsername());
    }

}
