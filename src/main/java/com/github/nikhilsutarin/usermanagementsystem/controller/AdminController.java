package com.github.nikhilsutarin.usermanagementsystem.controller;

import com.github.nikhilsutarin.usermanagementsystem.dto.RoleAssignmentDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.UserCreateDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.UserDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.UserUpdateDTO;
import com.github.nikhilsutarin.usermanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        UserDTO createdUser = userService.createUser(createDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserDTO updatedUser = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Assign roles to user
    @PostMapping("/users/{id}/roles")
    public ResponseEntity<UserDTO> assignRoles(@PathVariable Integer id, @Valid @RequestBody RoleAssignmentDTO roleAssignmentDTO) {
        UserDTO updatedUser = userService.assignRoles(id, roleAssignmentDTO);
        return ResponseEntity.ok(updatedUser);
    }

}
