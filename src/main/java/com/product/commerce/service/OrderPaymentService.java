package com.product.commerce.service;

import com.product.commerce.entity.*;
import com.product.commerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderPaymentService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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

        order.getItems().forEach(orderItem -> {
             Product product = orderItem.getProduct();
            product.increaseStock(orderItem.getQuantity());
            productRepository.save(product);
        });

        order.markCancel();
        orderRepository.save(order);
    }
}
