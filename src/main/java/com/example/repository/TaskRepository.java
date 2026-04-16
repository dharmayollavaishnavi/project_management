package com.example.repository;

import com.example.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByAssignedToId(Long userId);
	List<Task> findByAssignedToEmail(String email);
}