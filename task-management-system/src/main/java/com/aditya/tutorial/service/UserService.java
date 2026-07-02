package com.aditya.tutorial.service;

import com.aditya.tutorial.dto.pagination.PageResponse;
import com.aditya.tutorial.dto.teamDtos.AddTeamDto;
import com.aditya.tutorial.dto.userDtos.UserDto;
import com.aditya.tutorial.dto.userDtos.UserResponseDto;
import com.aditya.tutorial.dto.userDtos.UserRoleRequestDto;
import com.aditya.tutorial.entity.Enums.Roles;
import com.aditya.tutorial.entity.Team;
import com.aditya.tutorial.entity.User;
import com.aditya.tutorial.exceptions.AdminRoleException;
import com.aditya.tutorial.exceptions.InvalidRequestException;
import com.aditya.tutorial.exceptions.ResourceNotFoundException;
import com.aditya.tutorial.repo.TeamRepo;
import com.aditya.tutorial.repo.UserRepo;
import com.aditya.tutorial.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final TeamRepo teamRepo;


//    public UserDto createEmployee(UserDto userDto) {
//       String email= userDto.getEmail();
//   Optional<User> sameEmail= userRepo.findByEmail(email);
//            if(sameEmail.isPresent()){
//                throw new UserAlreadyExistsException("user with same email exists");
//            }
//            else{
//                User user = modelMapper.map(userDto, User.class);
//                User savedUser = userRepo.save(user);
//
//                return modelMapper.map(savedUser, UserDto.class);
//            }
//
//
//
//
//    }

    public PageResponse<UserResponseDto> getAll(Roles roles,Long teamId,Pageable pageable) {

        Specification<User> specification=Specification.unrestricted();
        if(roles!=null){
            specification=specification.and(UserSpecification.hasRole(roles));
        }
        if(teamId!=null){
            specification=specification.and((UserSpecification.hasTeamId(teamId)));
        }
        Page<User> page;
        if(specification!=null){
            page=userRepo.findAll(specification,pageable);
        }else{
            page=userRepo.findAll(pageable);
        }

     List<UserResponseDto> userList= page.getContent().stream().map((element) -> modelMapper.map(element, UserResponseDto.class)).toList();
        PageResponse<UserResponseDto> pageResponse=new PageResponse<>();
        pageResponse.setContent(userList);
        pageResponse.setPageSize(page.getSize());
        pageResponse.setPageNo(page.getNumber());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setLast(page.isLast());

        return pageResponse;
    }

    @Cacheable(cacheNames="users",key ="#id")
    public UserResponseDto getEmpById(Long id) {
        User employee = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        return modelMapper.map(employee, UserResponseDto.class);

    }

    public int deleteById(Long id) {
        boolean val = userRepo.existsById(id);
        if(val){
            userRepo.deleteById(id);
            return 1;
        }else{
            return -1;
        }

    }

    public UserResponseDto UpdateEmployee( UserDto userDto,Long id) {
     User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with Id not found"));

       user.setName(userDto.getName());

     User updatedUser = userRepo.save(user);
     return modelMapper.map(updatedUser,UserResponseDto.class);


    }

    public UserResponseDto updateEmployeeRole(UserRoleRequestDto userRoleRequestDto, Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with Id now found"));

        if(user.getRole()!= Roles.ADMIN){
            user.setRole(userRoleRequestDto.getRole());
        }else{
            throw new AdminRoleException("Admin role cannot be changed");
        }
        User save = userRepo.save(user);
        return  modelMapper.map(save,UserResponseDto.class);
    }

    public UserResponseDto addEmpToTeam(AddTeamDto addTeamDto, Long id) {
      User user= userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User with Id not found"));
        Team team = teamRepo.findById(addTeamDto.getTeamId()).orElseThrow(()-> new ResourceNotFoundException("Team Not found"));
        if(user.getRole()!=Roles.ADMIN){
            user.setTeam(team);
            User savedUser = userRepo.save(user);

            return modelMapper.map(savedUser,UserResponseDto.class);
        }else{
            throw new InvalidRequestException("Admin can't be added to any team");
        }

    }
}
