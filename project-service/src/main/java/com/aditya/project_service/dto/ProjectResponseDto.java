package com.aditya.project_service.dto;


import com.aditya.project_service.entity.Enums.Status;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ProjectResponseDto {

    private Long id;
    private String name;
    private String description;
    private Timestamp created_at;
    private Status status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private TeamSummaryDto team;

}
