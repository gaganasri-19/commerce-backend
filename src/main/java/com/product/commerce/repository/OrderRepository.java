package com.product.commerce.repository;

import com.product.commerce.entity.Order;
import com.product.commerce.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
