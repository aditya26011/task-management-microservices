package com.aditya.user_service.dto;

import com.aditya.user_service.entity.enums.Roles;
import lombok.Data;

@Data
public class UserAuthDto {
    private Long id;

    private String email;

    private String password;

    private Roles role;
}
