package com.aditya.tutorial.specifications;

import com.aditya.tutorial.entity.Enums.Status;
import com.aditya.tutorial.entity.Project;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public static Specification<Project> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status);
    }

    public static Specification<Project> hasProjectId(Long teamId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team").get("id"),teamId);
    }
}
