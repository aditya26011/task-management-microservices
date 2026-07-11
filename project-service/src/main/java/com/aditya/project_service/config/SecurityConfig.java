package com.aditya.project_service.config;

import com.aditya.project_service.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aditya.project_service.entity.enums.Roles.MANAGER;

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

                        .requestMatchers(HttpMethod.GET, "/projects/**")
                        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        .requestMatchers(HttpMethod.PATCH, "/projects/**")
                        .hasRole("MANAGER")

                        .requestMatchers(HttpMethod.DELETE, "/projects/**")
                        .hasRole("MANAGER")



                        .requestMatchers(HttpMethod.POST,"/projects/**")
                        .hasRole(MANAGER.name())


                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    }
