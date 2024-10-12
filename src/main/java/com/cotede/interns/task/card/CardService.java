package com.cotede.interns.task.card;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public List<Card> createRoundCards(List<String> cardsText) throws JsonProcessingException {
        Stack<String> stack = new Stack<>();
        List<Card> cards = new ArrayList<Card>();
        for(String text : cardsText){
            Card card = CardUtility.extractCard(text);
            cards.add(card);
            stack.push(card.getObject());
        }

        // adding opposing object for each card
        for( Card card : cards){
            card.setOpposingObject(stack.pop());
        }

        return cardRepository.saveAll(cards);
    }

    public Optional<Card> getCardById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }
}
