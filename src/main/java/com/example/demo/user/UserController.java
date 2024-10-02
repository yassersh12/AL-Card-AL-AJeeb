package com.example.demo.user;

import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ApiResponse.<User>builder()
                .content(createdUser)
                .status(HttpStatus.CREATED)
                .message("User created successfully")
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> ApiResponse.<User>builder()
                        .content(user)
                        .status(HttpStatus.OK)
                        .message("User found")
                        .build())
                .orElseGet(() -> ApiResponse.<User>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("User not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ApiResponse.<List<User>>builder()
                .content(users)
                .status(HttpStatus.OK)
                .message("Users retrieved successfully")
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("User deleted successfully")
                .build();
    }
}

