package com.cotede.interns.task.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        return userRepository.save(userRequest);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserScoreDto> getTop10UsersByScore() {
        List<UserScoreDto> users = userRepository.findTop10ByScore();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setRank(i + 1); // Set rank starting from 1
        }
        return users;
    }

    public List<UserCreativityDto> getTop10UsersByAvgCreativity() {
        List<UserCreativityDto> users = userRepository.findTop10ByAvgCreativity();
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setRank(i + 1); // Set rank starting from 1
        }
        return users;
    }
}
