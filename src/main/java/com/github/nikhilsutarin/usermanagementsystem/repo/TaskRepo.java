package com.github.nikhilsutarin.usermanagementsystem.repo;

import com.github.nikhilsutarin.usermanagementsystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
}
