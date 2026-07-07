package com.aditya.task_service.dtos;

import com.aditya.task_service.entity.enums.Priority;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDto {

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "description is required")
    private String description;
    private Priority priority;

    private LocalDateTime dueDate;

    @NotEmpty(message = "projectId is required")
    private Long projectId;

    @NotEmpty(message = "userId is required")
    private Long userId;
}
