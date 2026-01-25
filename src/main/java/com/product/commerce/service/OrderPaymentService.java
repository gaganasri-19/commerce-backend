package com.product.commerce.service;

import com.product.commerce.cache.CacheKeys;
import com.product.commerce.entity.*;
import com.product.commerce.repository.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderPaymentService(OrderRepository orderRepository, ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
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
            redisTemplate.delete(CacheKeys.PRODUCT + product.getId());
            redisTemplate.delete(CacheKeys.PRODUCT_LIST);
        });

        order.markCancel();
        orderRepository.save(order);
    }
}
