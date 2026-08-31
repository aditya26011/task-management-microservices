package com.aditya.task_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSummaryDto {
    private Long projectId;
    private String name;
    private Long teamId;
}
