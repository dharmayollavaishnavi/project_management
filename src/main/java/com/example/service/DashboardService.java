package com.example.service;

import com.example.repository.ProjectRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    public Map<String, Object> getDashboardData() {

        Map<String, Object> data = new HashMap<>();

        long totalProjects = projectRepo.count();
        long totalTasks = taskRepo.count();

        long completedTasks = taskRepo.findAll()
                .stream()
                .filter(t -> "DONE".equalsIgnoreCase(t.getStatus()))
                .count();

        long pendingTasks = taskRepo.findAll()
                .stream()
                .filter(t -> !"DONE".equalsIgnoreCase(t.getStatus()))
                .count();

        long totalUsers = userRepo.count();

        data.put("totalProjects", totalProjects);
        data.put("totalTasks", totalTasks);
        data.put("completedTasks", completedTasks);
        data.put("pendingTasks", pendingTasks);
        data.put("totalUsers", totalUsers);

        return data;
    }
}