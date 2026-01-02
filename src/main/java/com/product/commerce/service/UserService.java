package com.product.commerce.service;

import com.product.commerce.entity.User;
import com.product.commerce.exception.ResourceAlreadyExistsException;
import com.product.commerce.dto.UserProfileResponse;
import com.product.commerce.dto.UserRegisterRequest;
import com.product.commerce.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository; // Dependency injection
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) { // Constructor injection
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getEmail(), hashedPassword);
        userRepository.save(user);
    }

    public UserProfileResponse getCurrentUser(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return new UserProfileResponse(user.getId(), user.getEmail());
}

}
