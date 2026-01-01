package com.product.commerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    protected User() {}  //JPA requires a default constructor to instantiate the entity using reflection (creates objects at runtime)

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getters only (no setters yet)
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
