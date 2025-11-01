package com.github.nikhilsutarin.usermanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAssignmentDTO {

    @NotNull(message = "Roles cannot be null")
    @Size(min = 1, message = "At least one role is required")
    private Set<String> roles;
}
