package com.aditya.tutorial.dto.projectDtos;

import com.aditya.tutorial.entity.Enums.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProjectRequestDto {

    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "description is required")
    private String description;

    @NotEmpty(message = "startDate is required")
    private Timestamp startDate;

    @NotEmpty(message = "endDate is required")
    private Timestamp endDate;

    @NotEmpty(message = "teamId is required")
    private Long teamId;


}
