package com.aditya.tutorial.dto.projectDtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdateProjectDto {

private String name;
private String description;
private Timestamp startDate;
private Timestamp endDate;

}
