package com.cotede.interns.task.openai;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final String API_KEY = "sk-v3SlrN1OvACt8sbO1jkPyc0EyHD4ta4fFtmsOwgpQOT3BlbkFJDVRw0MyD9QpG9osKV7mV7-G4Wj3A7mR7HcQR6rTSgA";

    private final String SYSTEM_MESSAGE = """
        You are an AI assistant designed to manage a game where two players compete by sending descriptions of how their object/creature would defeat their opponent's. Each round, you generate two random cards, each containing a description of a random object or creature and its opposing object or creature from the other card. You also generate a random environment for the battle.
        
        After each player submits their strategy, you evaluate their responses, assigning a damage output between 0 and 50 based on how effective their strategy is, with 50 representing the best possible attack. Both players start with 100 HP, and the game continues in rounds until one player’s HP reaches 0. If both reach 0 HP in the same round, the player with the higher remaining HP (even if negative) wins.
        
        You should also provide a special creativity grade that assesses the players' creativity throughout the game. At the end of each response, you must generate a game summary, containing the most important details from previous rounds to provide context for upcoming evaluations and card generations. Ensure that the summary includes damage values, creativity scores, and any other information necessary to balance the game.
        
        Make sure the response is properly formatted as valid JSON, for the cards and the evaluation so the information can be easily extracted. Cards has to have card1, card2 (object and cardDescription, for each), environment (fightingPlace, weather). the evaluation have to give userAttack1 and userAttack2 objects (in json) each having : (damage, creativity, description (of the whole attack)). And in every json response, the summary should be there under the name summary. 
        """;

    private String gameSummary = "";

    private  HttpEntity<Map<String, Object>> createOpenAiRequest(String fullPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_MESSAGE),
                Map.of("role", "user", "content", fullPrompt)
        ));
        requestBody.put("max_tokens", 2000);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        return request;
    }

    private String sendRequest(String fullPrompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> request  = createOpenAiRequest(fullPrompt);

        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        return OpenAiUtility.extractGeneratedText(response.getBody());
    }

    public AiCardsResponse generateCards() throws Exception {
        String fullPrompt = "Generate new cards and environment based on the following game summary: " + gameSummary;
        String jsonResponse = sendRequest(fullPrompt);
        AiCardsResponse cardResponse = OpenAiUtility.extractCardResponse(jsonResponse);
        gameSummary = cardResponse.getSummary();
        return cardResponse;
    }

    public AiEvaluationsResponse evaluateResponses(String prompt) throws Exception {
        String fullPrompt = "Evaluate the player responses based on the following game summary: " + gameSummary + ". " + prompt;
        String jsonResponse = sendRequest(fullPrompt);
        AiEvaluationsResponse evaluationResponse = OpenAiUtility.extractEvaluationResponse(jsonResponse);
        gameSummary = evaluationResponse.getSummary();
        return evaluationResponse;
    }
}
