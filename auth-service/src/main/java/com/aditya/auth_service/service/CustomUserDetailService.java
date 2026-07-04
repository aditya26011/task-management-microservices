package com.aditya.auth_service.service;

import com.aditya.auth_service.Auth.AuthUser;
import com.aditya.auth_service.client.UserClient;
import com.aditya.auth_service.dto.UserAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserClient userClient;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthDto user = userClient.getUserByEmail(email);

        return new AuthUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()  );
    }
    public AuthUser getUserById(Long id) {

        UserAuthDto dto = userClient.getUserById(id);

        return new AuthUser(
                dto.getId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole()
        );
    }
}
