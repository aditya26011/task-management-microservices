package com.aditya.user_service.controller;

import com.aditya.user_service.dto.CreateUserRequest;
import com.aditya.user_service.dto.UserAuthDto;
import com.aditya.user_service.dto.UserResponseDto;
import com.aditya.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/email/{email}")
    public UserAuthDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
    @GetMapping("/internal/users/{id}")
    public UserAuthDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
