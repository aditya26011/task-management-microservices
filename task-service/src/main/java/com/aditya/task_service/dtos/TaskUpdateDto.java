package com.aditya.task_service.dtos;

import com.aditya.task_service.entity.enums.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDto {
private String title;
private String description;
private Priority priority;
private LocalDateTime dueDate;

}
