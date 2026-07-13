package com.aditya.task_service.config;

import com.aditya.common_security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {



    private final JwtFilter jwtFilter;


    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{



        httpSecurity.csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET, "/tasks/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        .requestMatchers(HttpMethod.POST, "/tasks/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.PATCH, "/tasks/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/tasks/**")
                        .hasRole("MANAGER")

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    }
