package com.github.nikhilsutarin.usermanagementsystem.service;

import com.github.nikhilsutarin.usermanagementsystem.dto.TaskAssignmentDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.TaskCreateDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.TaskDTO;
import com.github.nikhilsutarin.usermanagementsystem.dto.TaskLimitedDTO;
import com.github.nikhilsutarin.usermanagementsystem.model.Task;
import com.github.nikhilsutarin.usermanagementsystem.model.User;
import com.github.nikhilsutarin.usermanagementsystem.repo.TaskRepo;
import com.github.nikhilsutarin.usermanagementsystem.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private UserRepo userRepo;

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());

        if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) {
            dto.setAssignedUserIds(task.getAssignedUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet()));
            dto.setAssignedUserNames(task.getAssignedUsers().stream()
                    .map(User::getName)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    // Manager Controller

    private TaskLimitedDTO convertToLimitedDTO(Task task) {
        TaskLimitedDTO dto = new TaskLimitedDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());

        return dto;
    }

    @Transactional
    public TaskLimitedDTO createTask(TaskCreateDTO createDTO) {
        Task task = new Task();
        task.setTitle(createDTO.getTitle());

        Task savedTask = taskRepo.save(task);
        return convertToLimitedDTO(savedTask);
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskDTO assignTaskToUsers(Integer taskId, TaskAssignmentDTO assignmentDTO) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Set<User> users = new HashSet<>();
        for (Integer userId : assignmentDTO.getUserIds()) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
            users.add(user);
            // Add task to user's task list
            user.getTasks().add(task);
        }

        task.setAssignedUsers(users);
        Task updatedTask = taskRepo.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public TaskDTO addUserToTask(Integer taskId, Integer userId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        task.getAssignedUsers().add(user);
        user.getTasks().add(task);

        Task updatedTask = taskRepo.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public TaskDTO removeUserFromTask(Integer taskId, Integer userId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        task.getAssignedUsers().remove(user);
        user.getTasks().remove(task);

        Task updatedTask = taskRepo.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Integer id) {

        Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if(task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()) {
            throw new RuntimeException("Unassign users to delete the task");
        }

        taskRepo.deleteById(id);
    }

    // User Controller

    public List<TaskLimitedDTO> getTasksLimitedByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getTasks().stream()
                .map(this::convertToLimitedDTO)
                .collect(Collectors.toList());
    }
}
