package com.aditya.tutorial.dto.projectDtos;

import com.aditya.tutorial.dto.teamDtos.TeamSummaryDto;
import com.aditya.tutorial.entity.Enums.Status;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ProjectResponseDto {

    private Long id;
    private String name;
    private String description;
    private Timestamp created_at;
    private Status status;

    private Timestamp startDate;
    private Timestamp endDate;

    private TeamSummaryDto team;

}
