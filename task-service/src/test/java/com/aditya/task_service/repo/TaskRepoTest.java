package com.aditya.task_service.repo;

import com.aditya.task_service.entity.Task;
import com.aditya.task_service.entity.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TaskRepoTest {

    @Autowired
    private TaskRepo taskRepo;



    @Test
    void testFindByAssignedUserId_ShouldReturnAssignedTasks() {

        //Arrange
        Task task1 =new Task();
        task1.setDescription("temporary Description");
        task1.setProjectId(1L);
        task1.setStatus(TaskStatus.TODO);
        task1.setAssignedUserId(21L);
        taskRepo.save(task1);

        Task task2 =new Task();
        task2.setDescription("temporary Description 2");
        task2.setProjectId(1L);
        task2.setStatus(TaskStatus.TODO);
        task2.setAssignedUserId(21L);
        taskRepo.save(task2);

        //Act
        List<Task> result = taskRepo.findByAssignedUserId(task1.getAssignedUserId());

        //Assert
        assertThat(result).isNotNull();
       assertThat(result).hasSize(2);



    }
}