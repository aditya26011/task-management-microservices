package com.aditya.auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponseDto {

    private Long id;
    private String email;
    private String token;
    private String refreshToken;

}
