package com.github.nikhilsutarin.usermanagementsystem.controller;

import com.github.nikhilsutarin.usermanagementsystem.dto.*;
import com.github.nikhilsutarin.usermanagementsystem.service.TaskService;
import com.github.nikhilsutarin.usermanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    // View all users with their assigned tasks
    @GetMapping("/users")
    public ResponseEntity<List<UserWithTasksDTO>> getAllUsersWithTasks() {
        List<UserWithTasksDTO> users = userService.getAllUsersWithTasks();
        return ResponseEntity.ok(users);
    }

    // View specific user with their tasks
    @GetMapping("/users/{id}")
    public ResponseEntity<UserWithTasksDTO> getUserWithTasks(@PathVariable Integer id) {
        UserWithTasksDTO user = userService.getUserWithTasks(id);
        return ResponseEntity.ok(user);
    }

    // Create a new task
    @PostMapping("/tasks")
    public ResponseEntity<TaskLimitedDTO> createTask(@Valid @RequestBody TaskCreateDTO createDTO) {
        TaskLimitedDTO createdTask = taskService.createTask(createDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Get all tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Assign task to multiple users
    @PostMapping("/tasks/{taskId}/assign")
    public ResponseEntity<TaskDTO> assignTaskToUsers(@PathVariable Integer taskId, @Valid @RequestBody TaskAssignmentDTO assignmentDTO) {
        TaskDTO assignedTask = taskService.assignTaskToUsers(taskId, assignmentDTO);
        return ResponseEntity.ok(assignedTask);
    }

    // Add a single user to a task
    @PostMapping("/tasks/{taskId}/users/{userId}")
    public ResponseEntity<TaskDTO> addUserToTask(@PathVariable Integer taskId, @PathVariable Integer userId) {
        TaskDTO updatedTask = taskService.addUserToTask(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Remove a user from a task
    @DeleteMapping("/tasks/{taskId}/users/{userId}")
    public ResponseEntity<TaskDTO> removeUserFromTask(@PathVariable Integer taskId, @PathVariable Integer userId) {
        TaskDTO updatedTask = taskService.removeUserFromTask(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete task - unassign all users first
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
