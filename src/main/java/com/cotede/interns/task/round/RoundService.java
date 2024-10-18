package com.cotede.interns.task.round;

import com.cotede.interns.task.card.Card;
import com.cotede.interns.task.card.CardService;
import com.cotede.interns.task.card.CardUtility;
import com.cotede.interns.task.environment.Environment;
import com.cotede.interns.task.environment.EnvironmentService;
import com.cotede.interns.task.openai.AiCardsResponse;
import com.cotede.interns.task.openai.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final OpenAiService openAiService;
    private final CardService cardService;
    private final EnvironmentService environmentService;
    private List<Round> rounds = new ArrayList<Round>();

    //---- > not finished yet  < ----
    public Round createRound() throws Exception {
        return roundRepository.save(new Round());
    }

    public RoundCreationResponse createRoundResponse() throws Exception {
        AiCardsResponse cardsJsonResponse = openAiService.generateCards();

        List<String> cardsText = RoundUtility.extractCardsText(cardsJsonResponse);
        List<Card> cards = cardService.createRoundCards(cardsText);

        String enviornmentText = cardsJsonResponse.getEnvironment();
        Environment environment = environmentService.createEnvironment(enviornmentText);

        return new RoundCreationResponse(cards, environment);
    }

    public Optional<Round> getRoundById(Long roundId) {
        return roundRepository.findById(roundId);
    }

    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }

    public void deleteRound(Long roundId) {
        roundRepository.deleteById(roundId);
    }
}
