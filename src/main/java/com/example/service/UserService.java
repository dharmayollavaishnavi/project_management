package com.example.service;

import com.example.entity.User;
import com.example.dto.UserDTO;
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

    // =========================
    // 🔄 ENTITY → DTO CONVERTER
    // =========================
    private UserDTO convertToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

    // =========================
    // 📄 GET ALL USERS (ADMIN ONLY)
    // =========================
    public List<UserDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // =========================
    // 🔍 GET USER BY ID (ADMIN ONLY)
    // =========================
    public UserDTO getById(Long id) {
        return convertToDTO(
                repo.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    // =========================
    // ✏️ UPDATE USER (ADMIN ONLY)
    // =========================
    public User update(Long id, User user) {

        User existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        // 🔐 Encode password ONLY if updated
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(encoder.encode(user.getPassword()));
        }

        existing.setRole(user.getRole());

        return repo.save(existing);
    }

    // =========================
    // ❌ DELETE USER (ADMIN ONLY)
    // =========================
    public void delete(Long id) {
        repo.deleteById(id);
    }
}