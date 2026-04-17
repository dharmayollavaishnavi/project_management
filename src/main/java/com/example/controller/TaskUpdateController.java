package com.example.controller;

import com.example.entity.TaskUpdate;
import com.example.service.TaskUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task-updates")
public class TaskUpdateController {

    @Autowired
    private TaskUpdateService service;

    // 👤 USER adds daily update
    @PostMapping("dailyprogress/{taskId}")
    public TaskUpdate addUpdate(@PathVariable Long taskId,
                                @RequestBody TaskUpdate update,
                                Authentication auth) {

        String email = auth.getName();

        return service.addUpdate(taskId, update, email);
    }
}