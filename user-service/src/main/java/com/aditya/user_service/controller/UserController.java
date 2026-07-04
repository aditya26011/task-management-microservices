package com.aditya.user_service.controller;


import com.aditya.user_service.dto.*;
import com.aditya.user_service.dto.pagination.PageResponse;
import com.aditya.user_service.entity.enums.Roles;
import com.aditya.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PostMapping("/create")
//    public ResponseEntity<UserDto> createEmployee(@Valid  @RequestBody UserDto userDto){
//         UserDto user=userService.createEmployee(userDto);
//
//         return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }
    @GetMapping()
    public ResponseEntity<PageResponse<UserResponseDto>> getAll(@RequestParam(required = false) Roles roles,
                                                                @RequestParam(required = false) Long teamId,
                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortDir,
                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                @RequestParam(defaultValue = "5")int pageSize){

        Sort sort=sortDir.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);
        return new ResponseEntity<>(userService.getAll(roles,teamId,pageable),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getEmpById(@PathVariable(value = "id") Long id){
        UserResponseDto user=userService.getEmpById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Long id){
      int deleted= userService.deleteById(id);
      if(deleted==1)
          return new ResponseEntity<>("deleted Successfully",HttpStatus.NO_CONTENT  );
      else
          return new ResponseEntity<>("failed to delete",HttpStatus.BAD_REQUEST);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> UpdateEmployee(@RequestBody UserDto userDto, @PathVariable(value = "id") Long id){
        UserResponseDto updatedEmployee =  userService.UpdateEmployee(userDto,id);
     return new ResponseEntity<>(updatedEmployee,HttpStatus.OK);
    }

    @PatchMapping("{id}/role")
    public ResponseEntity<UserResponseDto> updateEmployeeRole(@RequestBody UserRoleRequestDto userRoleRequestDto,
                                                              @PathVariable(value = "id") Long id){

        UserResponseDto updatedEmployeeData=userService.updateEmployeeRole(userRoleRequestDto,id);
        return new ResponseEntity<>(updatedEmployeeData,HttpStatus.OK);

    }


//    @PatchMapping("/{id}/assign-team")
//    public ResponseEntity<UserResponseDto> addEmpToTeam(@RequestBody AddTeamDto addTeamDto,
//                                                        @PathVariable("id")Long id){
//
//        UserResponseDto updatedUser=userService.addEmpToTeam(addTeamDto,id);
//        return ResponseEntity.ok(updatedUser);
//
//    }
}
