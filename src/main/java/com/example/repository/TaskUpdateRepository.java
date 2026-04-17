package com.example.repository;

import com.example.entity.TaskUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, Long> {

    List<TaskUpdate> findByTaskId(Long taskId);
}