package com.aditya.project_service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class UpdateProjectDto {

private String name;
private String description;
private LocalDateTime startDate;
private LocalDateTime endDate;

}
