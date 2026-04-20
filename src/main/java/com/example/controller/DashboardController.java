package com.example.controller;

import com.example.service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.getDashboardData();
    }
}