package com.cotede.interns.task.userAttack;

import com.cotede.interns.task.card.Card;
import com.cotede.interns.task.openai.AiUserAttack;
import com.cotede.interns.task.user.User;
import com.cotede.interns.task.user.UserResponse;
import com.cotede.interns.task.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAttackService {

    private final UserAttackRepository userAttackRepository;
    private final UserService userService;

    public UserAttack createUserAttack(UserResponse userResponse, Card card, AiUserAttack aiUserAttack) throws Exception {

        User user = userService.getUserByUsername(userResponse.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        String attackDescription = userResponse.getAttackDescription();
        Long damage = aiUserAttack.getDamage();
        Long creativity = aiUserAttack.getCreativity();

        return UserAttack.builder()
                .card(card)
                .user(user)
                .attackDescription(attackDescription)
                .damage(damage)
                .creativity(creativity)
                .build();
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
