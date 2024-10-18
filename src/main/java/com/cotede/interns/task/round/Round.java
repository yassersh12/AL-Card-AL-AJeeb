package com.cotede.interns.task.round;

import com.cotede.interns.task.environment.Environment;
import com.cotede.interns.task.game.Game;
import com.cotede.interns.task.userAttack.UserAttack;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RoundKey.class)
@Builder
public class Round {

    @Id
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Id
    private Long roundNumber;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    private Environment environment;

    @OneToMany(mappedBy = "round")
    private List<UserAttack> userAttacks;
}
