package com.yuvaintern.taskmanagement.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/health")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminHealth() {
        return Map.of("status", "ok", "access", "ADMIN");
    }
}
