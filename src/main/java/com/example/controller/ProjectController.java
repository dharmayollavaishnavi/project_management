package com.example.controller;

import com.example.entity.Project;
import com.example.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    // ✅ CREATE PROJECT (ADMIN)
    @PostMapping
    public Project create(@RequestBody Project p) {
        return service.create(p);
    }

    // ✅ GET ALL PROJECTS (ADMIN)
    @GetMapping
    public List<Project> getAll() {
        return service.getAll();
    }

    // ✅ GET PROJECT BY ID (ADMIN)
    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ UPDATE PROJECT (ADMIN)
    @PutMapping("/{id}")
    public Project update(@PathVariable Long id, @RequestBody Project p) {
        return service.update(id, p);
    }

    // ✅ DELETE PROJECT (ADMIN)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Project deleted successfully";
    }
}