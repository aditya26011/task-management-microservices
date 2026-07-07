package com.aditya.project_service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ProjectRequestDto {

    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "description is required")
    private String description;

    @NotEmpty(message = "startDate is required")
    private LocalDateTime startDate;

    @NotEmpty(message = "endDate is required")
    private LocalDateTime endDate;

    @NotEmpty(message = "teamId is required")
    private Long teamId;


}
