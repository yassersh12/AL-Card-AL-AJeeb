package com.cotede.interns.task.round;

import com.cotede.interns.task.openai.AiCardsResponse;
import com.cotede.interns.task.openai.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;
    private final OpenAiService openAiService;
    private List<Round> rounds;

    //---- > not finished yet  < ----
    public Round createRound() throws Exception {
        AiCardsResponse cardResponse = openAiService.generateCards();

        for(int i = 1; i <= 2; i++) {

        }

        return roundRepository.save(new Round());
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
