package com.yuvaintern.taskmanagement.dto;

import com.yuvaintern.taskmanagement.entity.Task;
import com.yuvaintern.taskmanagement.entity.TaskStatus;
import java.time.Instant;

public record TaskResponse(Long id, String title, String description, TaskStatus status,
                           String ownerEmail, Instant createdAt, Instant updatedAt) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
            task.getStatus(), task.getOwner().getEmail(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
