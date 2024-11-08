package com.cotede.interns.task.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top-scores")
    public ResponseEntity<List<UserScoreDto>> getTop10UsersByScore() {
        List<UserScoreDto> topUsers = userService.getTop10UsersByScore();
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/top-creativity")
    public ResponseEntity<List<UserCreativityDto>> getTop10UsersByAvgCreativity() {
        List<UserCreativityDto> topUsers = userService.getTop10UsersByAvgCreativity();
        return ResponseEntity.ok(topUsers);
    }
}
