package com.product.commerce.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product.commerce.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); 
}
