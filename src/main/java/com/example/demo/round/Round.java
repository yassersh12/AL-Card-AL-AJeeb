package com.example.demo.round;

import com.example.demo.environment.Environment;
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
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private int roundNumber;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    private Environment environment;

    //@OneToMany(mappedBy = "Round")
   // private List<UserAttack> userAttacks;


}
