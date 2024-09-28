package com.example.demo.userAttack;

import com.example.demo.card.Card;
import com.example.demo.game.Game;
import com.example.demo.round.Round;
import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAttack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attackId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String attackDescription;
    private Long damage;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "game_id", referencedColumnName = "game_id"),
            @JoinColumn(name = "roundNumber", referencedColumnName = "roundNumber")
    })
    private Round round;

}
