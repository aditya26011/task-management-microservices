package com.aditya.user_service.config;

import com.aditya.user_service.filter.JwtFilter;
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

                        // Internal APIs (temporarily allow them)
                        .requestMatchers("/internal/**").permitAll()

                        // User APIs
                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        .requestMatchers(HttpMethod.PATCH, "/users/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/users/**")
                        .hasRole("ADMIN")

                        // Team APIs
                        .requestMatchers(HttpMethod.GET, "/teams/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        .requestMatchers(HttpMethod.POST, "/teams/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.PATCH, "/teams/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/teams/**")
                        .hasRole("MANAGER")

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    }
