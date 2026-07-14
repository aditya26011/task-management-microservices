package com.aditya.user_service.dto;

import com.aditya.user_service.entity.enums.Roles;
import lombok.Data;

@Data
public class UserSummaryDto {
    private Long id;
    private String name;
    private Roles role;
    private Long teamId;
    private String email;
}
