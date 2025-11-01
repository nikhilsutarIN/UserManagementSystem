package com.github.nikhilsutarin.usermanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String title;
    private Set<Integer> assignedUserIds;
    private Set<String> assignedUserNames;
}
