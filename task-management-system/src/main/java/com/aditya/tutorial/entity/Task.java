package com.aditya.tutorial.entity;

import com.aditya.tutorial.entity.Enums.Priority;
import com.aditya.tutorial.entity.Enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @CreationTimestamp
    private LocalDateTime created_at;

    private LocalDateTime dueDate;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

}
