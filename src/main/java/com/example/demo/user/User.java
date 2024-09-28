package com.example.demo.user;

import com.example.demo.game.Game;
import com.example.demo.userAttack.UserAttack;
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
    private String role;

    @OneToMany(mappedBy = "player1")
    private List<Game> gamesAsPlayer1;

    @OneToMany(mappedBy = "player2")
    private List<Game> gamesAsPlayer2;

    @OneToMany(mappedBy = "user")
    private List<UserAttack> userAttacks;
}

