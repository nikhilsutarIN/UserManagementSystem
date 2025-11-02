# User Management System API with Role-Based Access Control

Test API:
[UserManagementSystemAPI](https://reqbin.com/grjcn8wz)

A Spring Boot application implementing user management with role-based access control, task management, and secure authentication.

## Features

- **User Management**: Create, read, update, and delete users
- **Role-Based Access Control**: Three levels - Admin, Manager, and User
- **Task Management**: Assign and track tasks for users
- **JWT Authentication**: BCrypt password hashing and JWT Authentication
- **RESTful APIs**: Different endpoints for different user roles
- **Pre-populated Data**: Sample users, roles, and tasks for testing

## Technologies Used

- **Spring Boot 3**
- **Spring Security** - Authentication and authorization
- **Spring Security OAuth2 Resource Server** – JWT validation and API security
- **Spring Data JPA** - Database operations
- **MySQL** - Relational database
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management

## Project Structure

```
src/main/java/com/github/nikhilsutarin/usermanagementsystem/
├── config/
│   ├── SecurityConfig.java               # Security configuration
│   ├── DataInitializer.java              # Sample data loader
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
├── controller/
│   ├── AdminController.java              # Admin endpoints
│   ├── AuthController.java               # Authentication
│   ├── ManagerController.java            # Manager endpoints
│   └── UserController.java               # User endpoints
├── dto/                                  # Data Transfer Objects
├── exception/                             
│   └── GlobalExceptionHandler.java       # Exception handling
├── jwt/
│   ├── CustomAccessDeniedHandler.java    # Access denied handling
│   ├── JwtAuthenticationConverter.java  
│   ├── JwtAuthenticationEntryPoint.java  # Invalid token handling
│   └── JwtUtil.java                      # Generate jwt token
├── model/
│   ├── User.java                         # User entity
│   ├── Role.java                         # Role entity
│   └── Task.java                         # Task entity
├── repository/                           # JPA repositories
├── service/
│   ├── UserService.java
│   └── TaskService.java
└── UserManagementApplication.java        # Main application
```

### Default Users

The system comes pre-populated with the following users:

| Role    | Email               | Password   |
|---------|---------------------|------------|
| Admin   | admin@example.com   | admin123   |
| Manager | manager@example.com | manager123 |
| User    | john@example.com    | user123    |
| User    | jane@example.com    | user123    |
| User    | bob@example.com     | user123    |

## API Endpoints

### Authentication API (Role: ANY)

**Base URL**: `/api/auth`

| Method | Endpoint | Description        |
|--------|----------|--------------------|
| POST   | `/login` | Generate jwt token |


### Admin APIs (Role: ADMIN)

**Base URL**: `/api/admin`

| Method | Endpoint              | Description           |
|--------|-----------------------|-----------------------|
| POST   | `/users`              | Create a new user     |
| GET    | `/users`              | Get all users         |
| GET    | `/users/{id}`         | Get user by ID        |
| PUT    | `/users/{id}`         | Update user           |
| DELETE | `/users/{id}`         | Delete user           |
| POST   | `/users/{id}/roles`   | Assign roles to user  |

### Manager APIs (Role: MANAGER)

**Base URL**: `/api/manager`

| Method | Endpoint                     | Description               |
|--------|------------------------------|---------------------------|
| GET    | `/users`                     | View all users with tasks |
| GET    | `/users/{id}`                | View a user with tasks    |
| POST   | `/tasks`                     | Create a new task         |
| GET    | `/tasks`                     | Get all tasks             |
| POST   | `/tasks/{taskId}/assign`     | Assign task to users      |
| POST   | `/tasks/{taskId}/users/{id}` | Assign task to a user     |
| DELETE | `/tasks/{taskId}/users/{id}` | Unassign task to a user   |
| DELETE | `/tasks/{taskId}`            | Delete task               |

### User APIs (Role: ANY)

**Base URL**: `/api/user`

| Method | Endpoint                    | Description                  |
|--------|-----------------------------|------------------------------|
| GET    | `/profile`                  | View own profile             |
| GET    | `/tasks`                    | View own tasks               |

## API Testing Examples

### 0.1. Authentication - Generate jwt token

NOTE: The token expires in 3 minutes

**Request:**

`POST http://localhost:8080/api/auth/login`

**Request Body:**

```json
{
   "email": "newuser@example.com",
   "password": "password123"
}
```

### 1.1. Admin - Create User

**Request:**

`POST http://localhost:8080/api/admin/users`

**Request Body:**

```json
{
   "name": "New User",
   "email": "newuser@example.com",
   "password": "password123"
}
```

### 1.2. Admin - Get All Users

**Request:**

`GET http://localhost:8080/api/admin/users`


### 1.3. Admin - Get User by Id

**Request:**

`GET http://localhost:8080/api/admin/users/1`

### 1.4. Admin - Update User

**Request:**

`PUT http://localhost:8080/api/admin/users/1`

**Request Body:**

```json
{
   "name": "Updated Name",
   "email": "updated@example.com"
}
```

### 1.5. Admin - Delete User

**Request:**

`DELETE http://localhost:8080/api/admin/users/1`

### 1.6. Admin - Assign Roles

**Request:**

`POST http://localhost:8080/api/admin/users/1/roles`

**Request Body:**

```json
{
   "roles": ["USER", "MANAGER"]
}
```

### 2.1. Manager - View All Users with Tasks

**Request:**

`GET http://localhost:8080/api/manager/users`

### 2.1. Manager - View User with Tasks

**Request:**

`GET http://localhost:8080/api/manager/users/1`

### 2.3. Manager - Create Task

**Request:**

`POST http://localhost:8080/api/manager/tasks`

**Request Body:**

```json
{
   "title": "New Task"
}
```

### 2.4. Manager - View All Tasks

**Request:**

`GET http://localhost:8080/api/manager/tasks`


### 2.5. Manager - Assign Task to Users

**Request:**

`POST http://localhost:8080/api/manager/tasks/1/assign`

**Request Body:**

```json
{
   "userIds": [1, 2]
}
```

### 2.6. Manager - Assign Task to User

**Request:**

`POST http://localhost:8080/api/manager/tasks/1/users/1`


### 2.7. Manager - Unassign Task to User

**Request:**

`DELETE http://localhost:8081/api/manager/tasks/1/users/1`

### 2.8. Manager - Delete Task

NOTE: Unassign all users first to delete task

**Request:**

`DELETE http://localhost:8081/api/manager/tasks/1`

### 3.1. User - View Own Profile

**Request:**

`GET http://localhost:8080/api/user/profile`

### 3.2. User - View Own Tasks

**Request:**

`GET http://localhost:8080/api/user/tasks`

## Security Configuration


### Role Hierarchy

1. **ADMIN**: 
    - Can create, read, update, and delete users
    - Can assign roles to users

2. **MANAGER**:
    - Can view all users and their tasks
    - Can create and assign tasks
    - Can unassign and delete tasks

3. **USER**: 
    - Can view own profile 
    - Can view own tasks

## Exception Handling

The application handles common errors:
- **400 Bad Request**: Invalid input data
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource doesn't exist
- **409 Conflict**: User already exists

## Testing

The application includes pre-populated test data:
- 3 roles (ADMIN, MANAGER, USER)
- 5 users with different roles
- 5 tasks assigned to users
