package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    // =========================
    // REGISTER USER
    // =========================
    public String register(User user) {

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        // FORCE USER ROLE
        user.setRole("USER");

        repo.save(user);

        return "User registered successfully";
    }

    // =========================
    // LOGIN USER
    // =========================
    public String login(String email, String password) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // ✔ return RAW role only
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    
   
    }
}