package com.product.commerce.service;

import com.product.commerce.entity.*;
import com.product.commerce.repository.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    @Transactional
    public Order createOrder(String userEmail, Long productId, int quantity) {

        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // Reduce stock FIRST
        product.reduceStock(quantity); //no need to save explicitly due to transactional context

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order(user, product, quantity);
        return orderRepository.save(order);
    }

    @Transactional
    public void markOrderPaid(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() != OrderStatus.CREATED) {
        throw new RuntimeException("Order cannot be paid");
    }

    order.setStatus(OrderStatus.PAID);
    orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() != OrderStatus.CREATED) {
        throw new RuntimeException("Only CREATED orders can be cancelled");
    }

    Product product = order.getProduct();
    product.setStock(product.getStock() + order.getQuantity());

    order.setStatus(OrderStatus.CANCELLED);

    productRepository.save(product);
    orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, String email) {

    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!order.getUser().getEmail().equals(email)) {
        throw new RuntimeException("Not allowed");
    }

    if (order.getStatus() != OrderStatus.CREATED) {
        throw new RuntimeException("Order cannot be cancelled");
    }

    // Restore stock
    Product product = order.getProduct();
    product.increaseStock(order.getQuantity());

    order.markCancel(); // sets status = CANCELLED
    }
}
