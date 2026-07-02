package com.aditya.tutorial.service;

import com.aditya.tutorial.entity.User;
import com.aditya.tutorial.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return   userRepo.findByEmail(username).orElseThrow(()->new BadCredentialsException("User with email not found"));
    }


    public User getUserById(Long userId){
           return userRepo.findById(userId).orElseThrow(()->new BadCredentialsException("User with email not found"));
    }
}
