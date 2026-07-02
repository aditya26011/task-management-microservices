package com.aditya.tutorial.dto.authDtos;


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
