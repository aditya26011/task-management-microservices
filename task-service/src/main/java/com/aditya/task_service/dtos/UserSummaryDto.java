package com.aditya.task_service.dtos;

import com.aditya.task_service.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDto {
    private Long id;
    private String name;
    private Roles role;
    private Long teamId;
    private String email;
}
