package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_updates")
public class TaskUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private Integer completionPercentage;
    private Integer pendingPercentage;
    private Integer estimatedDays;

    private LocalDateTime updatedAt;

    // 🔗 MANY UPDATES → ONE TASK
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    // 🔗 WHO UPDATED
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User updatedBy;

    public TaskUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Integer completionPercentage) { this.completionPercentage = completionPercentage; }

    public Integer getPendingPercentage() { return pendingPercentage; }
    public void setPendingPercentage(Integer pendingPercentage) { this.pendingPercentage = pendingPercentage; }

    public Integer getEstimatedDays() { return estimatedDays; }
    public void setEstimatedDays(Integer estimatedDays) { this.estimatedDays = estimatedDays; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public User getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
}