package com.aditya.user_service.entity;


import com.aditya.user_service.entity.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    @CreationTimestamp
    private LocalDateTime created_at;

//    @ManyToOne
//    @JoinColumn(name = "team_id")
//    private Team team; //fk team cannot exist without user
//
}
