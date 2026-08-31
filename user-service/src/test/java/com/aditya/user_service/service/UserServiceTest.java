package com.aditya.user_service.service;

import com.aditya.user_service.dto.*;
import com.aditya.user_service.dto.pagination.PageResponse;
import com.aditya.user_service.entity.Team;
import com.aditya.user_service.entity.User;
import com.aditya.user_service.entity.enums.Roles;
import com.aditya.user_service.exceptions.AdminRoleException;
import com.aditya.user_service.exceptions.ResourceNotFoundException;
import com.aditya.user_service.exceptions.UserAlreadyExistsException;
import com.aditya.user_service.repo.TeamRepo;
import com.aditya.user_service.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private TeamRepo teamRepo;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;





    @Test
    void testGetEmpById_WhenEmployeeIdIsPresent_ThenReturnUserResponseDto() {
        //assign
        Long id=1L;
        User user = User.builder()
                .id(id)
                .name("aditya")
                .email("aditya826@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        when(userRepo.findById(id))
                .thenReturn(Optional.of(user)); //stubbing
        //act
        UserAuthDto userById = userService.getUserById(id);

        //assert

        assertThat(userById.getId()).isEqualTo(id);
        assertThat(userById.getEmail()).isEqualTo(user.getEmail());

        verify(userRepo,atMost(1)).findById(id);
    }

    @Test
    void testGetEmpById_WhenEmployeeIdNotPresent_ThenThrowResourceNotFoundException(){
        //Arrange

        Long id=1L;

        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User Not Found");

        verify(userRepo).findById(id);

    }

    @Test
    void testCreateUser_ThenItShouldCreateUser(){

        CreateUserRequest request= CreateUserRequest
                                    .builder()
                .role(Roles.EMPLOYEE)
                .name("aditya")
                .email("aditya@gmail.com")
                .build();

        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        //assign

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenReturn(user);

        //assert
        UserResponseDto userResponseDto=userService.createUser(request);

        //act
        assertThat(userResponseDto.getEmail()).isEqualTo(request.getEmail());
        assertThat(userResponseDto.getRole()).isEqualTo(request.getRole());
        assertThat(userResponseDto.getName()).isEqualTo(request.getName());

        verify(userRepo).save(any(User.class));


    }

    @Test
    void testCreateUser_ThrowingUserExists(){
        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        CreateUserRequest request= CreateUserRequest
                .builder()
                .role(Roles.EMPLOYEE)
                .name("aditya")
                .email("aditya@gmail.com")
                .build();


        //assign
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThatThrownBy(()->userService.createUser(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists");

        verify(userRepo).findByEmail(request.getEmail());

    }

    @Test
    void testGetUserByEmail_WhenEmailIsPresent_ThenReturnUser(){

        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        //arrange
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        //act
        UserAuthDto userByEmail = userService.getUserByEmail(user.getEmail());
        //assert
        assertThat(userByEmail).isNotNull();
        assertThat(userByEmail.getEmail()).isEqualTo(user.getEmail());

        verify(userRepo).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmail_WhenEmailIsNotPresent_ThenThrowResourceNotFoundException(){
        String email="aditya12@gmail.com";

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        //act
        assertThatThrownBy(()->userService.getUserByEmail(email)).
                isInstanceOf(ResourceNotFoundException.class).
                hasMessage("User Not Found");
    }

    @Test
    void testUpdateEmployeeRole_WhenRoleIsNotAdmin_ThenUpdateRole(){

        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .id(1L)
                .role(Roles.EMPLOYEE)
                .build();

        UserRoleRequestDto userRoleRequestDto=UserRoleRequestDto
                .builder()
                .role(Roles.MANAGER)
                .build();
        //arrange
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        //act

        UserResponseDto userResponseDto1 = userService.updateEmployeeRole(userRoleRequestDto, user.getId());

        //assert

        assertThat(userResponseDto1).isNotNull();
        assertThat(userResponseDto1.getRole()).isEqualTo(Roles.MANAGER);
        verify(userRepo).findById(user.getId());
        verify(userRepo).save(user);

    }

    @Test
    void testUpdateEmployeeRole_WhenEmployeeRole_ThenThrowAdminRoleException(){
        User user=User.builder().role(Roles.ADMIN).id(1L).build();

        UserRoleRequestDto userRoleRequestDto=UserRoleRequestDto
                .builder()
                .role(Roles.MANAGER)
                .build();

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));

        assertThatThrownBy(()->userService.updateEmployeeRole(userRoleRequestDto,user.getId())).isInstanceOf(AdminRoleException.class).hasMessage("Admin role cannot be changed");
    }

    @Test
    void testUpdateEmployee_thenNameIsUpdated(){
        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .id(1L)
                .role(Roles.EMPLOYEE)
                .build();

        UserDto userDto=UserDto
                        .builder()
                .name("vishal")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        //assign
         when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
         when(userRepo.save(any(User.class))).thenReturn(user);

        //act
        UserResponseDto userResponseDto = userService.UpdateEmployee(userDto, user.getId());
        //assert
        assertThat(userResponseDto.getName()).isEqualTo(userDto.getName());
        verify(userRepo).save(argThat(savedUser ->
                savedUser.getName().equals("vishal")
        ));

    }
    @Test
    void testGetEmpById_WhenIdIsPresent_ThenReturnUserAuthDto(){
        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .id(1L)
                .role(Roles.EMPLOYEE)
                .build();




        //assign
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));

        //act

        UserAuthDto userById = userService.getUserById(user.getId());

        //assert
        assertThat(userById.getEmail()).isEqualTo(user.getEmail());
        assertThat(userById.getRole()).isEqualTo(user.getRole());

        verify(userRepo).findById(user.getId());

    }

    @Test
    void testAddEmpToTeam_ThenReturnUserResponseDto(){
        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .id(1L)
                .role(Roles.EMPLOYEE)
                .build();

        Team team=Team.builder()
                .name("Test Team")
                .description("Test description")
                .userList(Collections.singletonList(user))
                .build();

        AddTeamDto addTeamDto= AddTeamDto.builder()
                .teamId(2L)
                .build();
        //assign
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(teamRepo.findById(anyLong())).thenReturn(Optional.of(team));
        when(userRepo.save(any(User.class)))
                .thenReturn(user);
        //act
      UserResponseDto result= userService.addEmpToTeam(addTeamDto,user.getId());
        //assert
        assertThat(result).isNotNull();
        assertThat(user.getTeam()).isEqualTo(team);

        verify(userRepo).findById(user.getId());

        verify(teamRepo).findById(addTeamDto.getTeamId());

        verify(userRepo).save(user);

    }

    @Test
    void testGetEmpById_WhenIdIsPresent_ThenReturnEmp(){
        User user= User.builder()
                .name("aditya")
                .email("aditya@gmail.com")
                .id(1L)
                .role(Roles.EMPLOYEE)
                .build();
        //assign
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));

        //act
        UserResponseDto empById = userService.getEmpById(user.getId());

        //assert
        assertThat(empById).isNotNull();
        assertThat(empById.getName()).isEqualTo(user.getName());
        assertThat(empById.getRole()).isEqualTo(user.getRole());

        verify(userRepo).findById(user.getId());
    }

    @Test
    void testGetAll_WhenRoleAndTeamIdProvided_ThenReturnPaginatedUsers() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 2);

        User user1 = User.builder()
                .id(1L)
                .name("Aditya")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("Vishal")
                .email("vishal@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        Page<User> userPage = new PageImpl<>(
                List.of(user1, user2),
                pageable,
                5
        );

        UserResponseDto dto1 = UserResponseDto.builder()
                .id(1L)
                .name("Aditya")
                .email("aditya@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        UserResponseDto dto2 = UserResponseDto.builder()
                .id(2L)
                .name("Vishal")
                .email("vishal@gmail.com")
                .role(Roles.EMPLOYEE)
                .build();

        when(userRepo.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(userPage);

        when(modelMapper.map(user1, UserResponseDto.class))
                .thenReturn(dto1);

        when(modelMapper.map(user2, UserResponseDto.class))
                .thenReturn(dto2);

        // Act
        PageResponse<UserResponseDto> result =
                userService.getAll(
                        Roles.EMPLOYEE,
                        10L,
                        pageable
                );

        // Assert
        assertThat(result).isNotNull();

        assertThat(result.getContent()).hasSize(2);

        assertThat(result.getContent().get(0).getName())
                .isEqualTo("Aditya");

        assertThat(result.getContent().get(1).getName())
                .isEqualTo("Vishal");

        assertThat(result.getPageNo())
                .isEqualTo(0);

        assertThat(result.getPageSize())
                .isEqualTo(2);

        assertThat(result.getTotalElements())
                .isEqualTo(5);

        assertThat(result.getTotalPages())
                .isEqualTo(3);

        assertThat(result.isLast())
                .isFalse();

        // Verify repository interaction
        verify(userRepo).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }
    @Test
    void testDeleteById_WhenIdIsPresent_ThenDeleteEmp(){

       Long userId=1L;

        //assign
       when(userRepo.existsById(anyLong())).thenReturn(true);

       //act
        int res = userService.deleteById(userId);

        //assert
        assertThat(res).isEqualTo(1L);

        verify(userRepo).existsById(userId);
        verify(userRepo).deleteById(userId);
    }

    @Test
    void testDeleteById_WhenIdIsNotPresent_ThenReturnNegative(){
        Long id=12L;

        when(userRepo.existsById(anyLong())).thenReturn(false);

        int res=userService.deleteById(id);

        assertThat(res).isEqualTo(-1);
    }

}