package com.product.commerce.service;

import com.product.commerce.dto.CreateOrderRequest;
import com.product.commerce.dto.CreateOrderResponse;
import com.product.commerce.entity.*;
import com.product.commerce.event.OrderCreatedEvent;
import com.product.commerce.event.OrderEventPublisher;
import com.product.commerce.repository.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    @Transactional
    public CreateOrderResponse createOrder(String userEmail, CreateOrderRequest request) {
    User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

    Order order = new Order(user);

    for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
        System.out.println("Quantity: " + itemReq.getQuantity());

        Product product = productRepository.findByIdForUpdate(itemReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < itemReq.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product " + product.getName());
        }
        
        product.reduceStock(itemReq.getQuantity());

        OrderItem item = new OrderItem(
                order,
                product,
                itemReq.getQuantity(),
                product.getPrice()
        );

        order.addItem(item);
    }
    Order savedOrder = orderRepository.save(order);
    order.setSubtotal();
    order.setUser(user);

    orderEventPublisher.publishOrderCreated(
        new OrderCreatedEvent(
            savedOrder.getId(), 
            user.getEmail(),
            savedOrder.getSubtotal()));

    return new CreateOrderResponse(savedOrder.getId(), 
    user.getEmail(), savedOrder.getItems(), savedOrder.getSubtotal(), 
    savedOrder.getStatus(), savedOrder.getCreatedAt());
}

    @Transactional
    public void markOrderPaid(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() != OrderStatus.CREATED) {
        throw new RuntimeException("Order cannot be paid");
    }

    order.setStatus(OrderStatus.PAID);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() != OrderStatus.CREATED) {
        throw new RuntimeException("Only CREATED orders can be cancelled");
    }

    order.getItems().forEach(orderItem -> {
        Product product = orderItem.getProduct();
        product.increaseStock(orderItem.getQuantity());
        productRepository.save(product);
    });

    order.setStatus(OrderStatus.CANCELLED); //saved to db automatically due to transactional context

    }

}
