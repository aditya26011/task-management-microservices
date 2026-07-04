package com.aditya.auth_service.dto;

import com.aditya.auth_service.dto.enums.Roles;
import lombok.Data;

@Data
public class UserAuthDto {
    private Long id;

    private String email;

    private String password;

    private Roles role;
}
