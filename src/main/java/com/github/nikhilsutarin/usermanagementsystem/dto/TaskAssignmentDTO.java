package com.github.nikhilsutarin.usermanagementsystem.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentDTO {

    @NotNull(message = "User IDs cannot be null")
    @NotEmpty(message = "User IDs cannot be empty")
    @Size(min = 1, max = 50, message = "User IDs size must be between 1 and 50")
    private Set<Integer> userIds;
}
