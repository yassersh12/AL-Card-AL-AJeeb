package com.cotede.interns.task.round;

import com.cotede.interns.task.card.Card;
import com.cotede.interns.task.environment.Environment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class RoundCreationResponse {
    private List<Card> cards;
    private Environment environment;
}
