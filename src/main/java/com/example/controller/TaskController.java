package com.example.controller;

import com.example.dto.TaskDTO;
import com.example.entity.Task;
import com.example.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    // =========================
    // 🔐 ADMIN APIs
    // =========================

    @PostMapping("/admin")
    public TaskDTO create(@RequestBody Task t) {
        return service.create(t);
    }

    @PutMapping("/admin/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody Task t) {
        return service.update(id, t);
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }

    @GetMapping("/admin")
    public List<TaskDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/admin/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }
    
 // ✅ ADMIN: Get tasks by user ID
    @GetMapping("/admin/user/{userId}")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long userId) {
        return service.getTasksByUserId(userId);
    }

    // =========================
    // 👤 USER APIs (SECURE)
    // =========================

    // 🔥 FIXED: user can ONLY see their own tasks
    @GetMapping("/user/my-tasks")
    public List<TaskDTO> getMyTasks(Authentication auth) {

        String email = auth.getName(); // comes from JWT

        return service.getTasksByEmail(email);
    }

    // 🔥 USER: update only their own task status
    @PutMapping("/user/status/{id}")
    public TaskDTO updateStatus(@PathVariable Long id,
                                @RequestBody Task t,
                                Authentication auth) {

        String email = auth.getName();

        return service.updateStatus(id, t.getStatus(), email);
    }
}