package com.product.commerce.service;

import com.product.commerce.entity.*;
import com.product.commerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final OrderRepository orderRepository;

    public OrderPaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
 }

    @Transactional
    public void markPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.markPaid();
        orderRepository.save(order);
    }

    @Transactional
    public void markFailed(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = order.getProduct();
        product.increaseStock(order.getQuantity());

        order.markCancel();
        orderRepository.save(order);
    }
}
