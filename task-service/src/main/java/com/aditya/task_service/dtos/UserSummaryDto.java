package com.aditya.task_service.dtos;

import com.aditya.task_service.entity.enums.Roles;
import lombok.Data;

@Data
public class UserSummaryDto {
    private Long id;
    private String name;
    private Roles role;
    private Long teamId;
}
