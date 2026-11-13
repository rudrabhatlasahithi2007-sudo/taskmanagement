package com.yuvaintern.taskmanagement.controller;

import com.yuvaintern.taskmanagement.dto.*;
import com.yuvaintern.taskmanagement.entity.TaskStatus;
import com.yuvaintern.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(Authentication auth, @Valid @RequestBody TaskRequest request) {
        return taskService.create(auth.getName(), request);
    }

    @GetMapping
    public PageResponse<TaskResponse> getMine(Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) TaskStatus status) {
        return taskService.getMyTasks(auth.getName(), page, size, sortBy, direction, status);
    }

    @GetMapping("/{id}")
    public TaskResponse getOne(Authentication auth, @PathVariable Long id) {
        return taskService.getById(auth.getName(), id);
    }

    @PutMapping("/{id}")
    public TaskResponse update(Authentication auth, @PathVariable Long id,
                               @Valid @RequestBody TaskRequest request) {
        return taskService.update(auth.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable Long id) {
        taskService.delete(auth.getName(), id);
    }
}
