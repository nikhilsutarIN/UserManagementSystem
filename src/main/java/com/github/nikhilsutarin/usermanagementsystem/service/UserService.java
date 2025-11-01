package com.github.nikhilsutarin.usermanagementsystem.service;

import com.github.nikhilsutarin.usermanagementsystem.dto.*;
import com.github.nikhilsutarin.usermanagementsystem.model.Role;
import com.github.nikhilsutarin.usermanagementsystem.model.User;
import com.github.nikhilsutarin.usermanagementsystem.repo.RoleRepo;
import com.github.nikhilsutarin.usermanagementsystem.repo.TaskRepo;
import com.github.nikhilsutarin.usermanagementsystem.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }

    // Admin Controller

    @Transactional
    public UserDTO createUser(UserCreateDTO createDTO) {
        if (userRepo.existsByEmail(createDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        User user = new User();
        user.setName(createDTO.getName());
        user.setEmail(createDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setRoles(new HashSet<>());

        User savedUser = userRepo.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Integer id, UserUpdateDTO updateDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
            if (userRepo.existsByEmail(updateDTO.getEmail())) {
                throw new DataIntegrityViolationException("Email already exists");
            }
            user.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }

        User updatedUser = userRepo.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepo.existsById(id)) {
            throw new UsernameNotFoundException("User not found");
        }

        userRepo.deleteById(id);
    }

    @Transactional
    public UserDTO assignRoles(Integer id, RoleAssignmentDTO roleAssignmentDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleAssignmentDTO.getRoles()) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles);
        User updatedUser = userRepo.save(user);
        return convertToDTO(updatedUser);
    }

    private UserWithTasksDTO convertToUserWithTasksDTO(User user) {
        UserWithTasksDTO dto = new UserWithTasksDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        dto.setTasks(user.getTasks().stream()
                .map(task -> {
                    TaskDTO taskDTO = new TaskDTO();
                    taskDTO.setId(task.getId());
                    taskDTO.setTitle(task.getTitle());
                    taskDTO.setAssignedUserIds(task.getAssignedUsers().stream()
                            .map(User::getId)
                            .collect(Collectors.toSet()));
                    taskDTO.setAssignedUserNames(task.getAssignedUsers().stream()
                            .map(User::getName)
                            .collect(Collectors.toSet()));
                    return taskDTO;
                })
                .collect(Collectors.toSet()));
        return dto;
    }

    // Manager Controller

    public List<UserWithTasksDTO> getAllUsersWithTasks() {
        return userRepo.findAll().stream()
                .map(this::convertToUserWithTasksDTO)
                .collect(Collectors.toList());
    }

    public UserWithTasksDTO getUserWithTasks(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToUserWithTasksDTO(user);
    }

    private UserWithTasksLimitedDTO convertToUserWithTasksLimitedDTO(User user) {
        UserWithTasksLimitedDTO dto = new UserWithTasksLimitedDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        dto.setTasks(user.getTasks().stream()
                .map(task -> {
                    TaskLimitedDTO taskDTO = new TaskLimitedDTO();
                    taskDTO.setId(task.getId());
                    taskDTO.setTitle(task.getTitle());

                    return taskDTO;
                })
                .collect(Collectors.toSet()));
        return dto;
    }

    // User Controller

    public UserWithTasksLimitedDTO getUserWithLimitedTasks(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserWithTasksLimitedDTO(user);
    }

}
