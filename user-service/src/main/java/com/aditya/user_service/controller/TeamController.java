package com.aditya.user_service.controller;


import com.aditya.user_service.dto.pagination.PageResponse;
import com.aditya.user_service.dto.teamDtos.TeamResponseDto;
import com.aditya.user_service.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @PostMapping()
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamResponseDto teamResponseDto){
        TeamResponseDto teamResponseDto1=teamService.createTeam(teamResponseDto);
        return ResponseEntity.ok(teamResponseDto1);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponse<TeamResponseDto>> getAllTeams(@RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String sortDir,
                                                                     @RequestParam(defaultValue = "0") int pageNo,
                                                                     @RequestParam(defaultValue = "5")int pageSize){
        Sort sort=sortDir.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        return new ResponseEntity<>(teamService.getAllTeams(pageable),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable(value = "id")Long id){
        TeamResponseDto teamResponseDto=teamService.getTeamById(id);
        return ResponseEntity.ok(teamResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable(value = "id") Long id){
        boolean isDeleted =teamService.deleteTeam(id);
        if(isDeleted){
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>("Failed to Delete",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TeamResponseDto> patchTeam(@PathVariable(value = "id") Long id,
                                                     @RequestBody TeamResponseDto teamResponseDto){

            TeamResponseDto updatedTeam=teamService.patchTeam(id,teamResponseDto);
            return ResponseEntity.ok(updatedTeam);
    }
}
