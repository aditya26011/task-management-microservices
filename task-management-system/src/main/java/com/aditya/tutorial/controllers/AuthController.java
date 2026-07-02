package com.aditya.tutorial.controllers;

import com.aditya.tutorial.dto.authDtos.LoginDto;
import com.aditya.tutorial.dto.authDtos.LoginResponseDto;
import com.aditya.tutorial.dto.authDtos.SignUpDto;
import com.aditya.tutorial.dto.authDtos.SignUpResponseDto;
import com.aditya.tutorial.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpDto signUpDto){
        SignUpResponseDto responseDto=authService.signUp(signUpDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){

      LoginResponseDto loginResponseDto= authService.login(loginDto);
        Cookie cookie=new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

      return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest httpRequest){
      String refreshToken=  Arrays.stream(httpRequest.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(()->new AuthenticationServiceException("Refresh Token not found inside the cookie"));

        LoginResponseDto loginResponseDto= authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);

    }

}
