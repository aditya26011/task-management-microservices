package com.aditya.tutorial.specifications;

import com.aditya.tutorial.entity.Enums.Roles;
import com.aditya.tutorial.entity.Team;
import com.aditya.tutorial.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasRole(Roles roles) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"),roles);
    }

    public static Specification<User> hasTeamId(Long teamId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team").get("id"),teamId);
    }
}
