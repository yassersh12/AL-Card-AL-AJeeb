package com.example.demo.card;

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
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String object;
    private String opposingObject;
    private String cardDescription;

    @OneToMany(mappedBy = "card")
    private List<UserAttack> userAttacks;


}
