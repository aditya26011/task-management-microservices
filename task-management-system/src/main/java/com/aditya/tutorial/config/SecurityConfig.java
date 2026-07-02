package com.aditya.tutorial.config;


import com.aditya.tutorial.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aditya.tutorial.entity.Enums.Roles.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(auth->
                //Auth APIs
                auth.requestMatchers("/auth/**")
                        .permitAll()
                        .requestMatchers("/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll()
                        //Admin APIs
//                        .requestMatchers(HttpMethod.PATCH,"/user/**")
//                        .hasRole(ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE,"/user/**")
                        .hasRole(ADMIN.name())

                        //Managers
                        .requestMatchers(HttpMethod.POST,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.GET,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.DELETE,"/teams/**")
                        .hasRole(MANAGER.name())
                        .requestMatchers(HttpMethod.PATCH,"/user/**")
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
                        .requestMatchers(HttpMethod.GET,"/user/**")
                        .hasAnyRole(ADMIN.name(), MANAGER.name(), EMPLOYEE.name())

                        //Anything else
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form->form.disable())
                .csrf(csrfConfig->csrfConfig.disable())
                .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
