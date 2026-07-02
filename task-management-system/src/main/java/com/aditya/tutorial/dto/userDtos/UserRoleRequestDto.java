package com.aditya.tutorial.dto.userDtos;

import com.aditya.tutorial.entity.Enums.Roles;
import lombok.Data;

@Data
public class UserRoleRequestDto {
    private Roles role;
}
