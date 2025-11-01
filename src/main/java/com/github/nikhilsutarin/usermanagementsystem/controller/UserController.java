package com.github.nikhilsutarin.usermanagementsystem.controller;

import com.github.nikhilsutarin.usermanagementsystem.dto.TaskLimitedDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.UserWithTasksLimitedDTO;
import com.github.nikhilsutarin.usermanagementsystem.service.TaskService;
import com.github.nikhilsutarin.usermanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    // View own profile
    @GetMapping("/profile")
    public ResponseEntity<UserWithTasksLimitedDTO> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        UserWithTasksLimitedDTO user = userService.getUserWithLimitedTasks(email);
        return ResponseEntity.ok(user);
    }

    // View own assigned tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskLimitedDTO>> getMyTasks(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        List<TaskLimitedDTO> tasks = taskService.getTasksLimitedByEmail(email);
        return ResponseEntity.ok(tasks);
    }
}
