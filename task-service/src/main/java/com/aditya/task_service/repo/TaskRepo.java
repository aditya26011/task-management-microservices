package com.aditya.task_service.repo;


import com.aditya.task_service.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByAssignedUserId(Long assignedUserId);
    Page<Task> findAll(Pageable pageable);
}
