package com.cotede.interns.task.game;

import com.cotede.interns.task.user.User;
import com.cotede.interns.task.round.Round;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private User player2;

    private GameStatus status;

    private Long Player1Creativity;
    private Long Player2Creativity;

    private String battleDescription;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @OneToMany(mappedBy = "game")
    private List<Round> rounds;


}

