package com.example.demo.userAttack;

import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-attacks")
public class UserAttackController {

    @Autowired
    private UserAttackService userAttackService;

    @PostMapping
    public ApiResponse<UserAttack> createUserAttack(@RequestBody UserAttack userAttack) {
        UserAttack createdUserAttack = userAttackService.createUserAttack(userAttack);
        return ApiResponse.<UserAttack>builder()
                .content(createdUserAttack)
                .status(HttpStatus.CREATED)
                .message("User attack created successfully")
                .build();
    }

    @GetMapping("/{userAttackId}")
    public ApiResponse<UserAttack> getUserAttackById(@PathVariable Long userAttackId) {
        return userAttackService.getUserAttackById(userAttackId)
                .map(userAttack -> ApiResponse.<UserAttack>builder()
                        .content(userAttack)
                        .status(HttpStatus.OK)
                        .message("User attack found")
                        .build())
                .orElseGet(() -> ApiResponse.<UserAttack>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("User attack not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<UserAttack>> getAllUserAttacks() {
        List<UserAttack> userAttacks = userAttackService.getAllUserAttacks();
        return ApiResponse.<List<UserAttack>>builder()
                .content(userAttacks)
                .status(HttpStatus.OK)
                .message("User attacks retrieved successfully")
                .build();
    }

    @DeleteMapping("/{userAttackId}")
    public ApiResponse<Void> deleteUserAttack(@PathVariable Long userAttackId) {
        userAttackService.deleteUserAttack(userAttackId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("User attack deleted successfully")
                .build();
    }
}
