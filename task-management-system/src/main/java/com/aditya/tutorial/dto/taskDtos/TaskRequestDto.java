package com.aditya.tutorial.dto.taskDtos;

import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import com.aditya.tutorial.entity.Project;
import com.aditya.tutorial.entity.User;
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
