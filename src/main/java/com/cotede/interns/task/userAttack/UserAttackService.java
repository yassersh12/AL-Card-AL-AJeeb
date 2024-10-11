package com.cotede.interns.task.userAttack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAttackService {

    @Autowired
    private UserAttackRepository userAttackRepository;

    public UserAttack createUserAttack(UserAttack userAttack) {
        return userAttackRepository.save(userAttack);
    }

    public Optional<UserAttack> getUserAttackById(Long userAttackId) {
        return userAttackRepository.findById(userAttackId);
    }

    public List<UserAttack> getAllUserAttacks() {
        return userAttackRepository.findAll();
    }

    public void deleteUserAttack(Long userAttackId) {
        userAttackRepository.deleteById(userAttackId);
    }
}
