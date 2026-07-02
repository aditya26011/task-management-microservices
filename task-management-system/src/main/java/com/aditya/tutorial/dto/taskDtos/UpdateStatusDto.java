package com.aditya.tutorial.dto.taskDtos;

import com.aditya.tutorial.entity.Enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateStatusDto {
    private TaskStatus updateStatus;
}
