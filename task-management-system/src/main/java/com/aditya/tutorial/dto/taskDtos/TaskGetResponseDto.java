package com.aditya.tutorial.dto.taskDtos;

import com.aditya.tutorial.dto.projectDtos.ProjectSummaryDto;
import com.aditya.tutorial.dto.userDtos.UserSummaryDto;
import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskGetResponseDto {
    private Long id;

    private String title;

    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime created_at;

    private LocalDateTime dueDate;

    private ProjectSummaryDto project;

    private UserSummaryDto user;
}
