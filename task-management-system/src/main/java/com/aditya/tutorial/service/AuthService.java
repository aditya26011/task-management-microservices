package com.aditya.tutorial.service;


import com.aditya.tutorial.dto.authDtos.LoginDto;
import com.aditya.tutorial.dto.authDtos.LoginResponseDto;
import com.aditya.tutorial.dto.authDtos.SignUpDto;
import com.aditya.tutorial.dto.authDtos.SignUpResponseDto;
import com.aditya.tutorial.entity.User;
import com.aditya.tutorial.exceptions.UserAlreadyExistsException;
import com.aditya.tutorial.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.aditya.tutorial.entity.Enums.Roles.EMPLOYEE;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailService userDetailService;

    public SignUpResponseDto signUp(SignUpDto signUpDto) {
        String email= signUpDto.getEmail();
        Optional<User> sameEmail= userRepo.findByEmail(email);
        if(sameEmail.isPresent()){
            throw new UserAlreadyExistsException("user with same email exists");
        }
        else{

            User user = modelMapper.map(signUpDto, User.class);
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setRole(EMPLOYEE);
            User savedUser = userRepo.save(user);
            return modelMapper.map(savedUser,SignUpResponseDto.class);

        }

    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        User user= (User) authenticate.getPrincipal();
       String accessToken= jwtService.generateAccessToken(user);
       String refreshToken= jwtService.generateRefreshToken(user);
        return new LoginResponseDto(user.getId(),user.getEmail(),accessToken,refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId=jwtService.getUserIdFromToken(refreshToken);
        User user=userDetailService.getUserById(userId);

        String accessToken= jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(),user.getEmail(),accessToken,refreshToken);

    }
}
