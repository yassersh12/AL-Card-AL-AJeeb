package com.cotede.interns.task.userAttack;

import com.cotede.interns.task.card.Card;
import com.cotede.interns.task.user.User;
import com.cotede.interns.task.round.Round;
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
