package com.github.nikhilsutarin.usermanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithTasksDTO {
    private Integer id;
    private String name;
    private String email;
    private Set<String> roles;
    private Set<TaskDTO> tasks;
}
