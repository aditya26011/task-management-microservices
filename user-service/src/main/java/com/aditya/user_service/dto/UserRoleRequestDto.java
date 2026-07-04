package com.aditya.user_service.dto;

import com.aditya.user_service.entity.enums.Roles;
import lombok.Data;

@Data
public class UserRoleRequestDto {
    private Roles role;
}
