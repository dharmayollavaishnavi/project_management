package com.example.dto;

public class AuthResponse {

    private String token;

    // Constructor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter only (no setter needed usually)
    public String getToken() {
        return token;
    }
}