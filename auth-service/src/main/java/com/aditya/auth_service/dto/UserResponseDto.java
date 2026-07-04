package com.aditya.auth_service.dto;

import com.aditya.auth_service.dto.enums.Roles;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserResponseDto implements Serializable {
    private Long id;

    private String name;


    private String email;

    private Roles role;
    private Long teamId;

    private LocalDateTime created_at;
}
