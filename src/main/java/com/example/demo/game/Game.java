package com.example.demo.game;

import com.example.demo.round.Round;
import com.example.demo.user.User;
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
    private Long Player2creativity;

    private String battleDescription;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @OneToMany(mappedBy = "game")
    private List<Round> rounds;


}

