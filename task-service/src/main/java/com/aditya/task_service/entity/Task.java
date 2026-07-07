package com.aditya.task_service.entity;

import com.aditya.task_service.entity.enums.Priority;
import com.aditya.task_service.entity.enums.TaskStatus;
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


    private Long projectId;

    private Long assignedUserId;
}
