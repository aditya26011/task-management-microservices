package com.aditya.task_service.dtos;

import lombok.Data;

@Data
public class ProjectSummaryDto {
    private Long projectId;
    private String name;
    private Long teamId;
}
