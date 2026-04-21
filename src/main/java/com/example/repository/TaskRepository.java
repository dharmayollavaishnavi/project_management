package com.example.repository;

import com.example.entity.Task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByAssignedToId(Long userId);
	List<Task> findByAssignedToEmail(String email);
	
	// Filter by status + priority
	List<Task> findByStatusAndPriority(String status, String priority);

	// Filter by status only
	List<Task> findByStatus(String status);

	// Filter by priority only
	List<Task> findByPriority(String priority);

	// Search by title (case-insensitive)
	List<Task> findByTitleContainingIgnoreCase(String title);
	
	List<Task> findByDeadline(LocalDate deadline);
	List<Task> findByDeadlineAndStatusNot(LocalDate deadline, String status);
}