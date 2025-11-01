package com.github.nikhilsutarin.usermanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {

    @NotBlank(message = "Title is required")
    @Length(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;
}
