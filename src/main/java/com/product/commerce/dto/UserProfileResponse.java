package com.product.commerce.dto;

public class UserProfileResponse {

    private final Long id;
    private final String email;

    public UserProfileResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
