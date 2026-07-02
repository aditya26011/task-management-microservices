package com.aditya.tutorial.dto.taskDtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AssignTaskDto {
    @NotEmpty(message = "employeeId is required")
    private Long employeeId;
}
