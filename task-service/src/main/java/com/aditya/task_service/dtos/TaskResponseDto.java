package com.aditya.task_service.dtos;


import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.TaskStatus;
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
