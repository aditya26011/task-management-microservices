package com.aditya.tutorial.dto.authDtos;

import com.aditya.tutorial.entity.Enums.Roles;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpResponseDto {

    private Long id;
    private String name;
    private Roles role;
}
