package com.aditya.auth_service.client;

import com.aditya.auth_service.dto.CreateUserRequest;
import com.aditya.auth_service.dto.UserAuthDto;
import com.aditya.auth_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/internal/users/email/{email}")
    UserAuthDto getUserByEmail(@PathVariable String email);

    @PostMapping("/internal/users")
    UserResponseDto createUser(@RequestBody CreateUserRequest createUserRequest);

    @GetMapping("/internal/users/{id}")
    UserAuthDto getUserById(@PathVariable Long id);
}
