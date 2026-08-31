package com.aditya.project_service.dto;


import com.aditya.project_service.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectResponseDto implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Timestamp created_at;
    private Status status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private TeamSummaryDto team;

}
