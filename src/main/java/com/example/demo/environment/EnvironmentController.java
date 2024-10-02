package com.example.demo.environment;

import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @PostMapping
    public ApiResponse<Environment> createEnvironment(@RequestBody Environment environment) {
        Environment createdEnvironment = environmentService.createEnvironment(environment);
        return ApiResponse.<Environment>builder()
                .content(createdEnvironment)
                .status(HttpStatus.CREATED)
                .message("Environment created successfully")
                .build();
    }

    @GetMapping("/{environmentId}")
    public ApiResponse<Environment> getEnvironmentById(@PathVariable Long environmentId) {
        return environmentService.getEnvironmentById(environmentId)
                .map(environment -> ApiResponse.<Environment>builder()
                        .content(environment)
                        .status(HttpStatus.OK)
                        .message("Environment found")
                        .build())
                .orElseGet(() -> ApiResponse.<Environment>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("Environment not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<Environment>> getAllEnvironments() {
        List<Environment> environments = environmentService.getAllEnvironments();
        return ApiResponse.<List<Environment>>builder()
                .content(environments)
                .status(HttpStatus.OK)
                .message("Environments retrieved successfully")
                .build();
    }

    @DeleteMapping("/{environmentId}")
    public ApiResponse<Void> deleteEnvironment(@PathVariable Long environmentId) {
        environmentService.deleteEnvironment(environmentId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("Environment deleted successfully")
                .build();
    }
}
