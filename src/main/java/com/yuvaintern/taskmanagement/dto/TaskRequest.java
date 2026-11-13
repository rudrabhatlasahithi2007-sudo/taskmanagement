package com.yuvaintern.taskmanagement.dto;

import com.yuvaintern.taskmanagement.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotBlank @Size(max = 150) String title,
    @Size(max = 2000) String description,
    TaskStatus status
) {}
