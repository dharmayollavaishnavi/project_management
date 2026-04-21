package com.example.controller;

import com.example.entity.User;
import com.example.dto.UserDTO;   // ✅ IMPORTANT IMPORT
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    // =========================
    // 📄 GET ALL USERS (ADMIN ONLY)
    // =========================
    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAll();
    }

    // =========================
    // 🔍 GET USER BY ID (ADMIN ONLY)
    // =========================
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // =========================
    // ✏️ UPDATE USER BY ID (ADMIN ONLY)
    // =========================
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return service.update(id, user);
    }

    // =========================
    // ❌ DELETE USER BY ID(ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "User deleted successfully";
    }
}