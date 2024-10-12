package com.cotede.interns.task.round;

import com.cotede.interns.task.openai.AiCardsResponse;

import java.util.ArrayList;
import java.util.List;

public class RoundUtility {
    public static List<String> extractCardsText(AiCardsResponse cardsJsonResponse) {
        List<String> cardsText = new ArrayList<String>();
        cardsText.add(cardsJsonResponse.getCard1());
        cardsText.add(cardsJsonResponse.getCard2());
        return cardsText;
    }

}
