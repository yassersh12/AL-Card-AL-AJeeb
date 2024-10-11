package com.cotede.interns.task.user;

import com.cotede.interns.task.userAttack.UserAttack;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private Long score;
    private Long health = 100l;
    private String username;
    private String password;
    private Long AvgCreativity;
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<UserAttack> userAttacks;
}

