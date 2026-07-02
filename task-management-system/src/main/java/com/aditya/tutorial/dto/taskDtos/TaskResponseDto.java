package com.aditya.tutorial.dto.taskDtos;

import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import com.aditya.tutorial.entity.Project;
import com.aditya.tutorial.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {

    private Long id;

    private String title;

    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime created_at;

    private LocalDateTime dueDate;

    private Long projectId;
    private Long userId;

}
