package com.product.commerce.service;

import com.product.commerce.entity.User;
import com.product.commerce.exception.ResourceAlreadyExistsException;
import com.product.commerce.dto.UserRegisterRequest;
import com.product.commerce.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository; // Dependency injection
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) { // Constructor injection
        this.userRepository = userRepository;
    }

    public void register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getEmail(), hashedPassword);
        userRepository.save(user);
    }
}
