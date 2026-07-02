package com.aditya.tutorial.dto.taskDtos;

import com.aditya.tutorial.entity.Enums.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDto {
private String title;
private String description;
private Priority priority;
private LocalDateTime dueDate;

}
