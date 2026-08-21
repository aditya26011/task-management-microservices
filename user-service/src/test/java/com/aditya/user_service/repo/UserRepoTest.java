package com.aditya.user_service.repo;

import com.aditya.user_service.entity.User;
import com.aditya.user_service.entity.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User user;

    @BeforeEach
    void setUp(){
        user= User.builder()
                .name("aditya")
                .email("adityapawar826@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnUser() {

        //Arrange
         userRepo.save(user);
        //Act
        Optional<User> byEmail = userRepo.findByEmail(user.getEmail());
        //Assert
        assertThat(byEmail).isNotEmpty();
    }

    @Test
    void testFindByEmail_whenEmailIsNotValid_thenNoUser() {
      //Arrange
        String email="abcd@gmail.com";
       //Act
        Optional<User> byEmail = userRepo.findByEmail(email);
        //Assert
        assertThat(byEmail).isEmpty();
    }
}