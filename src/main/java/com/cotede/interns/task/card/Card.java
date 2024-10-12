package com.cotede.interns.task.card;

import com.cotede.interns.task.userAttack.UserAttack;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String object;
    private String opposingObject = "";
    private String cardDescription;

}
