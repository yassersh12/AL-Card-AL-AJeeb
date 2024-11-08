package com.cotede.interns.task.round;

import com.cotede.interns.task.card.Card;
import com.cotede.interns.task.card.CardService;
import com.cotede.interns.task.card.CardUtility;
import com.cotede.interns.task.environment.Environment;
import com.cotede.interns.task.environment.EnvironmentService;
import com.cotede.interns.task.game.Game;
import com.cotede.interns.task.game.GameRepository;
import com.cotede.interns.task.openai.AiCardsResponse;
import com.cotede.interns.task.openai.AiEvaluationsResponse;
import com.cotede.interns.task.openai.AiUserAttack;
import com.cotede.interns.task.openai.OpenAiService;
import com.cotede.interns.task.user.User;
import com.cotede.interns.task.user.UserResponse;
import com.cotede.interns.task.user.UserService;
import com.cotede.interns.task.user.UserUtility;
import com.cotede.interns.task.userAttack.UserAttack;
import com.cotede.interns.task.userAttack.UserAttackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final GameRepository gameRepository;
    private final OpenAiService openAiService;
    private final CardService cardService;
    private final EnvironmentService environmentService;
    private final UserAttackService userAttackService;
    private RoundCreationResponse currentRound;
    private Long roundNumber = 1L;


    public Round createRound(List<UserResponse> userResponses, Long gameId) throws Exception {
        List<UserAttack> userAttacks = new ArrayList<>();
        Game game = gameRepository.getReferenceById(gameId);
        String prompt = UserUtility.userResponsesToPrompt(userResponses);
        AiEvaluationsResponse evaluationsJsonResponse = openAiService.evaluateResponses(prompt);
        List<AiUserAttack> aiUserAttacks = evaluationsJsonResponse.getAiUserAttacks();
        String summary = evaluationsJsonResponse.getSummary();
        for (int i = 0; i < 2; i++) {
            Card card = currentRound.getCards().get(i);
            UserAttack userAttack = userAttackService.createUserAttack(userResponses.get(i), card, aiUserAttacks.get(i));
            System.out.println("Ai User Attack : " + aiUserAttacks.get(i).getDamage());
            System.out.println("User Attack : " + userAttack.getDamage());
            userAttacks.add(userAttack);
        }

        Round round = Round.builder()
                .game(game)
                .roundNumber(GetRoundNumber())
                .environment(currentRound.getEnvironment())
                .userAttacks(userAttacks)
                .build();
        roundRepository.save(round);
        round.setSummary(summary);
        return round;
    }

    private Long GetRoundNumber(){
        return roundNumber++;
    }

    public RoundCreationResponse createRoundResponse() throws Exception {
        AiCardsResponse cardsJsonResponse = openAiService.generateCards();
        List<String> cardsText = RoundUtility.extractCardsText(cardsJsonResponse);
        List<Card> cards = cardService.createRoundCards(cardsText);
        System.out.println(cardsText);
        System.out.println("//////////////////");
        System.out.println(cards.get(0).getObject());

        String enviornmentText = cardsJsonResponse.getEnvironment();
        System.out.println(enviornmentText);
        Environment environment = environmentService.createEnvironment(enviornmentText);

        currentRound = new RoundCreationResponse(cards, environment);

        return currentRound;
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
