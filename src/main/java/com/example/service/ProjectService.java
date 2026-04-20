package com.example.service;

import com.example.entity.Project;
import com.example.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repo;

    // =========================
    // CREATE PROJECT (ADMIN ONLY)
    // =========================
    public Project create(Project p) {

        // 🔐 DEADLINE IS MANDATORY (ADMIN FIELD)
        if (p.getDeadline() == null) {
            throw new RuntimeException("Deadline is required (Admin only field)");
        }

        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        return repo.save(p);
    }

    // =========================
    // GET ALL PROJECTS
    // =========================
    public List<Project> getAll() {
        return repo.findAll();
    }

    // =========================
    // GET PROJECT BY ID
    // =========================
    public Project getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    // =========================
    // UPDATE PROJECT (ADMIN ONLY)
    // =========================
    public Project update(Long id, Project p) {

        Project existing = getById(id);

        // BASIC FIELDS UPDATE
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setClientName(p.getClientName());
        existing.setStatus(p.getStatus());

        // 🔐 ADMIN ONLY: DEADLINE CONTROL
        if (p.getDeadline() != null) {
            existing.setDeadline(p.getDeadline());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // =========================
    // DELETE PROJECT
    // =========================
    public void delete(Long id) {
        repo.deleteById(id);
    }
}