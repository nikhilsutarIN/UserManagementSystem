package com.github.nikhilsutarin.usermanagementsystem.config;

import com.github.nikhilsutarin.usermanagementsystem.model.Role;
import com.github.nikhilsutarin.usermanagementsystem.model.Task;
import com.github.nikhilsutarin.usermanagementsystem.model.User;
import com.github.nikhilsutarin.usermanagementsystem.repo.RoleRepo;
import com.github.nikhilsutarin.usermanagementsystem.repo.TaskRepo;
import com.github.nikhilsutarin.usermanagementsystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {
//public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
    public void run(String... args) throws Exception {

        // Create roles
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Role managerRole = new Role();
        managerRole.setName("MANAGER");
        Role userRole = new Role();
        userRole.setName("USER");

        roleRepo.save(adminRole);
        roleRepo.save(managerRole);
        roleRepo.save(userRole);

        System.out.println("Roles created successfully!");

        // Create admin user
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(adminRoles);

        userRepo.save(admin);
        System.out.println("Admin user created - Email: admin@example.com, Password: admin123");

        // Create manager user
        Set<Role> managerRoles = new HashSet<>();
        managerRoles.add(managerRole);

        User manager = new User();
        manager.setName("Manager User");
        manager.setEmail("manager@example.com");
        manager.setPassword(passwordEncoder.encode("manager123"));
        manager.setRoles(managerRoles);

        userRepo.save(manager);
        System.out.println("Manager user created - Email: manager@example.com, Password: manager123");

        // Create users
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setPassword(passwordEncoder.encode("user123"));
        user1.setRoles(userRoles);

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setPassword(passwordEncoder.encode("user123"));
        user2.setRoles(userRoles);

        User user3 = new User();
        user3.setName("Bob Wilson");
        user3.setEmail("bob@example.com");
        user3.setPassword(passwordEncoder.encode("user123"));
        user3.setRoles(userRoles);

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);

        System.out.println("Regular users created:");
        System.out.println("  - john@example.com, Password: user123");
        System.out.println("  - jane@example.com, Password: user123");
        System.out.println("  - bob@example.com, Password: user123");

        // Create tasks
        Task task1 = new Task();
        task1.setTitle("Complete Project Documentation");
        taskRepo.save(task1);

        Task task2 = new Task();
        task2.setTitle("Review Code Changes");
        taskRepo.save(task2);

        Task task3 = new Task();
        task3.setTitle("Database Migration");
        taskRepo.save(task3);

        Task task4 = new Task();
        task4.setTitle("API Testing");
        taskRepo.save(task4);

        Task task5 = new Task();
        task5.setTitle("UI Updates");
        taskRepo.save(task5);

        System.out.println("Tasks created successfully!");

        // Assign tasks to users
        user1.getTasks().add(task1);
        user1.getTasks().add(task2);

        user2.getTasks().add(task1);
        user2.getTasks().add(task3);
        user2.getTasks().add(task4);

        user3.getTasks().add(task3);
        user3.getTasks().add(task5);

        // Save users
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);

        System.out.println("Tasks assigned to users successfully!");
        System.out.println("\n========================================");
        System.out.println("Application initialized successfully!");
        System.out.println("========================================\n");
    }
}
