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
    
   // admin creates task
    @PostMapping("/admin")
    public TaskDTO create(@RequestBody Task t) {
        return service.create(t);
    }
    
    //admin update task by task id
    @PutMapping("/admin/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody Task t) {
        return service.update(id, t);
    }

    //admin deletes task by task id
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }

    //admin views all tasks
    @GetMapping("/admin")
    public List<TaskDTO> getAll() {
        return service.getAll();
    }

    //admin views all tasks by task id
    @GetMapping("/admin/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // admin get tasks by user ID
    @GetMapping("/admin/user/{userId}")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long userId) {
        return service.getTasksByUserId(userId);
    }

    // admin search task by status, priority, title(search & filter)
 
    @GetMapping("/admin/search")
    public List<TaskDTO> searchTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String title) {

        return service.searchTasks(status, priority, title);
    }

    // =========================
    // 👤 USER APIs (SECURE)
    // =========================

    // 🔥 USER: can ONLY see their own tasks
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