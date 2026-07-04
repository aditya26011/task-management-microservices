package com.aditya.auth_service.service;



import com.aditya.auth_service.Auth.AuthUser;
import com.aditya.auth_service.client.UserClient;
import com.aditya.auth_service.dto.*;
import com.aditya.auth_service.dto.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private  final UserClient userClient;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;

    public SignUpResponseDto signUp(SignUpDto signUpDto) {
        CreateUserRequest request = new CreateUserRequest();

        request.setName(signUpDto.getName());
        request.setEmail(signUpDto.getEmail());
        request.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        request.setRole(Roles.EMPLOYEE);

        UserResponseDto response = userClient.createUser(request);

        return modelMapper.map(response, SignUpResponseDto.class);

    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        AuthUser user= (AuthUser) authenticate.getPrincipal();
       String accessToken= jwtService.generateAccessToken(user);
       String refreshToken= jwtService.generateRefreshToken(user);
        return new LoginResponseDto(user.getId(),user.getEmail(),accessToken,refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId=jwtService.getUserIdFromToken(refreshToken);
        AuthUser user=customUserDetailService.getUserById(userId);

        String accessToken= jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(),user.getEmail(),accessToken,refreshToken);

    }
}
