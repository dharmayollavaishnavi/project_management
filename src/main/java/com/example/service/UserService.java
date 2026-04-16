package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    // 📄 GET ALL USERS (ADMIN ONLY)
    public List<User> getAll() {
        return repo.findAll();
    }

    // 🔍 GET USER BY ID (ADMIN ONLY)
    public User getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✏️ UPDATE USER (ADMIN ONLY)
    public User update(Long id, User user) {

        User existing = getById(id);

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        // 🔐 Encode password ONLY if updated
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(encoder.encode(user.getPassword()));
        }

        existing.setRole(user.getRole());

        return repo.save(existing);
    }

    // ❌ DELETE USER (ADMIN ONLY)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}