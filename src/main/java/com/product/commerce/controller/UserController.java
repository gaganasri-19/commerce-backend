package com.product.commerce.controller;

import com.product.commerce.dto.UserProfileResponse;
import com.product.commerce.dto.UserRegisterRequest;
import com.product.commerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        userService.register(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.getCurrentUser(userDetails.getUsername());
}

}
