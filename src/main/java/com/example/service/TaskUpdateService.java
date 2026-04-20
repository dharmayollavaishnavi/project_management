package com.example.service;

import com.example.dto.TaskProgressDTO;
import com.example.entity.Task;
import com.example.entity.TaskUpdate;
import com.example.entity.User;
import com.example.repository.TaskRepository;
import com.example.repository.TaskUpdateRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.format.DateTimeFormatter;

@Service
public class TaskUpdateService {

    @Autowired
    private TaskUpdateRepository repo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    // =========================
    // ✅ ADD TASK UPDATE
    // =========================
    public TaskUpdate addUpdate(Long taskId, TaskUpdate update, String email) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 SECURITY
        if (!task.getAssignedTo().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        // =========================
        // ✅ VALIDATION LOGIC
        // =========================
        String status = update.getStatus().toUpperCase();

        if ("TO_DO".equals(status)) {
            if (update.getCompletionPercentage() != null ||
                update.getPendingPercentage() != null ||
                update.getEstimatedDays() != null) {

                throw new RuntimeException("Progress not allowed for TO_DO tasks");
            }
        }

        if ("IN_PROGRESS".equals(status)) {
            if (update.getCompletionPercentage() == null ||
                update.getPendingPercentage() == null ||
                update.getEstimatedDays() == null) {

                throw new RuntimeException("Progress details required for IN_PROGRESS");
            }
        }

        if ("DONE".equals(status)) {
            if (update.getCompletionPercentage() == null ||
                update.getCompletionPercentage() != 100) {

                throw new RuntimeException("Completion must be 100% for DONE tasks");
            }

            if (update.getPendingPercentage() != null &&
                update.getPendingPercentage() != 0) {

                throw new RuntimeException("Pending must be 0 for DONE tasks");
            }
        }

        if (update.getCompletionPercentage() != null &&
            update.getPendingPercentage() != null &&
            update.getCompletionPercentage() + update.getPendingPercentage() != 100) {

            throw new RuntimeException("Completion + Pending must be 100%");
        }

        // =========================
        // ✅ UPDATE TASK STATUS
        // =========================
        task.setStatus(status);
        taskRepo.save(task);

        // =========================
        // ✅ SAVE UPDATE
        // =========================
        update.setTask(task);
        update.setUpdatedBy(user);

        TaskUpdate savedUpdate = repo.save(update);

        // =========================
        // 📧 SEND EMAIL TO ADMINS
        // =========================
        List<User> admins = userRepo.findByRole("ADMIN");

        for (User admin : admins) {
            emailService.sendEmail(
                admin.getEmail(),
                "Task Updated",
                "Task: " + task.getTitle() +
                "\nUpdated by: " + user.getEmail() +
                "\nStatus: " + status
            );
        }

        // =========================
        // 📧 SEND CONFIRMATION TO USER
        // =========================
        emailService.sendEmail(
            user.getEmail(),
            "Task Update Submitted",
            "You updated task: " + task.getTitle() +
            "\nStatus: " + status
        );

        return savedUpdate;
    }

    // =========================
    // 📊 TASK PROGRESS (DTO CLEAN RESPONSE)
    // =========================
    public List<TaskProgressDTO> getProgressByTask(Long taskId) {

        // ✅ Validate task exists
        taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // ✅ FORMATTERS
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return repo.findByTaskId(taskId)
                .stream()
                .map(update -> {

                    TaskProgressDTO dto = new TaskProgressDTO();

                    dto.setStatus(update.getStatus());
                    dto.setCompletion(update.getCompletionPercentage());
                    dto.setPending(update.getPendingPercentage());

                    // ✅ FIXED: separate date & time
                    dto.setDate(update.getUpdatedAt().format(dateFormatter));
                    dto.setTime(update.getUpdatedAt().format(timeFormatter));

                    return dto;
                })
                .toList();
    }
}