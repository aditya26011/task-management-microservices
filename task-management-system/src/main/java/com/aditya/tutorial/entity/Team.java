package com.aditya.tutorial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @CreationTimestamp
    private Timestamp created_at;

    @OneToMany(mappedBy = "team")
    private List<User> userList;

    @OneToMany(mappedBy = "team")
    private List<Project> projectList ;
}
