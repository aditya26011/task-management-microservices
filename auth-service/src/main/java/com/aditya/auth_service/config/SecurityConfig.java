package com.aditya.auth_service.config;


import com.aditya.auth_service.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aditya.auth_service.dto.enums.Roles.*;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.DELETE,"/users/**")
                        .hasRole(ADMIN.name())

                        //Managers
                        .requestMatchers(HttpMethod.POST,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.GET,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.PATCH,"/users/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.POST,"/project/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.POST,"/task/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.GET,"/task/**")
                        .hasAnyRole(MANAGER.name(), EMPLOYEE.name())
                        .requestMatchers(HttpMethod.PATCH,"/task/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE,"/task/**")
                        .hasRole(MANAGER.name())

                        //All authenticated user
                        .requestMatchers(HttpMethod.GET,"/users/**")
                        .hasAnyRole(ADMIN.name(), MANAGER.name(), EMPLOYEE.name())

                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
