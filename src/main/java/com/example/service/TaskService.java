package com.example.service;

import com.example.dto.TaskDTO;
import com.example.entity.Task;
import com.example.entity.Project;
import com.example.entity.User;
import com.example.repository.TaskRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private EmailService emailService;

    // =========================
    // VALID STATUS
    // =========================
    private static final Set<String> VALID_STATUS =
            Set.of("TO_DO", "IN_PROGRESS", "DONE");

    // =========================
    // ENTITY → DTO
    // =========================
    private TaskDTO convertToDTO(Task task) {

        TaskDTO dto = new TaskDTO();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDeadline(task.getDeadline());

        if (task.getProject() != null) {
            dto.setProjectName(task.getProject().getName());
        }

        if (task.getAssignedTo() != null) {
            dto.setAssignedUserName(task.getAssignedTo().getName());
        }

        return dto;
    }

    // =========================
    // ADMIN: CREATE TASK
    // =========================
    public TaskDTO create(Task t) {

        if (t.getProject() == null || t.getProject().getId() == null)
            throw new RuntimeException("Project ID is required");

        if (t.getAssignedTo() == null || t.getAssignedTo().getId() == null)
            throw new RuntimeException("Assigned user ID is required");

        if (t.getDeadline() == null)
            throw new RuntimeException("Deadline required (Admin only)");

        Project project = projectRepo.findById(t.getProject().getId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepo.findById(t.getAssignedTo().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        t.setProject(project);
        t.setAssignedTo(user);

        t.setStatus(t.getStatus() == null ? "TO_DO" : t.getStatus().toUpperCase());

        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());

        Task savedTask = repo.save(t);

     // 📧 SEND EMAIL TO USER
     emailService.sendEmail(
         user.getEmail(),
         "New Task Assigned",
         "Task: " + savedTask.getTitle() +
         "\nProject: " + project.getName() +
         "\nDeadline: " + savedTask.getDeadline()
     );

     return convertToDTO(savedTask);
    }

    // =========================
    // ADMIN: GET ALL TASKS
    // =========================
    public List<TaskDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN: GET BY ID
    // =========================
    public TaskDTO getById(Long id) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return convertToDTO(task);
    }

    // =========================
    // ADMIN: UPDATE TASK
    // =========================
    public TaskDTO update(Long id, Task t) {

        Task existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existing.setTitle(t.getTitle());
        existing.setDescription(t.getDescription());
        existing.setPriority(t.getPriority());

        if (t.getStatus() != null) {
            existing.setStatus(t.getStatus().toUpperCase());
        }

        if (t.getDeadline() != null) {
            existing.setDeadline(t.getDeadline());
        }

        if (t.getProject() != null && t.getProject().getId() != null) {
            Project project = projectRepo.findById(t.getProject().getId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            existing.setProject(project);
        }

        if (t.getAssignedTo() != null && t.getAssignedTo().getId() != null) {
            User user = userRepo.findById(t.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existing.setAssignedTo(user);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return convertToDTO(repo.save(existing));
    }

    // =========================
    // ADMIN: DELETE TASK
    // =========================
    public void delete(Long id) {
        repo.deleteById(id);
    }
 // =========================
 // ADMIN: GET TASKS BY USER ID
 // =========================
 public List<TaskDTO> getTasksByUserId(Long userId) {

     // Optional: validate user exists
     userRepo.findById(userId)
             .orElseThrow(() -> new RuntimeException("User not found"));

     return repo.findByAssignedToId(userId)
             .stream()
             .map(this::convertToDTO)
             .collect(Collectors.toList());
 }

    // =========================
    // USER: GET OWN TASKS
    // =========================
    public List<TaskDTO> getTasksByEmail(String email) {
        return repo.findByAssignedToEmail(email)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // USER: UPDATE STATUS ONLY
    // =========================
    public TaskDTO updateStatus(Long id, String status, String email) {

        Task task = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // SECURITY CHECK
        if (!task.getAssignedTo().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        String normalized = status.toUpperCase();

        if (!VALID_STATUS.contains(normalized)) {
            throw new RuntimeException("Invalid status");
        }

        task.setStatus(normalized);
        task.setUpdatedAt(LocalDateTime.now());

        return convertToDTO(repo.save(task));
    }
}