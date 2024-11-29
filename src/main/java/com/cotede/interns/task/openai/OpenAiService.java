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

    private final String BASE_SYSTEM_MESSAGE = """
    You are an AI assistant designed to manage a game where two players compete by sending descriptions of how their object/creature would defeat their opponent's. Each round, you generate two random cards, each containing a description of a random object or creature and its opposing object or creature from the other card. You also generate a random environment for the battle.

    After each player submits their strategy, you evaluate their responses, assigning a damage output between 0 and 50 based on how effective their strategy is, with 50 representing the best possible attack. Both players start with 100 HP, and the game continues in rounds until one player’s HP reaches 0. If both reach 0 HP in the same round, the player with the higher remaining HP (even if negative) wins.

    You should also provide a special creativity grade that assesses the players' creativity throughout the game. At the end of each evaluation response, and only evaluation responses (not card generation), you must generate a game summary as a text (not subsequent json tree), these summaries are only necessary for other chatgpt instances that will handel other rounds. Ensure that the summary includes damage values, creativity scores, all previous rounds object names, environment, and any other information necessary to balance the game.

    Make sure the response is properly formatted as valid JSON for the cards and the evaluation so the information can be easily extracted. In every "evaluation" JSON response, the summary should be included under the name "summary".""";

    private final String CARDS_PROMPT = """
    Generate two random cards and an environment for the upcoming round.
    If this wasn't the first round, make sure that the cards are unique from the previous rounds provided by the game summary passed by the user prompt, and that they are not always mythical creatures and can be random creatures/objects from modern world.
    Each card should describe an object or creature and its opposing object or creature from the other card, in the format below.
    
    Response Format:
    {
        "card1": {
          "object": "<description of the first object>",
          "cardDescription": "<description of the first object's abilities>"
        },
        "card2": {
          "object": "<description of the second object>",
          "cardDescription": "<description of the second object's abilities>"
        },
        "environment": {
          "fightingPlace": "<location of the battle>",
          "weather": "<current weather conditions>"
        }
      
    }
    """;

    private final String EVALUATION_PROMPT = """
    Evaluate the players' attacks for the current round. Each attack should have a damage score (0-50), a creativity score, and a brief description of the attack. Use the format below.

    Response Format:
    {
        "userAttack1": {
          "damage": "<damage output for player 1>",
          "creativity": "<creativity score for player 1>",
          "description": "<description of the first player's attack>"
        },
        "userAttack2": {
          "damage": "<damage output for player 2>",
          "creativity": "<creativity score for player 2>",
          "description": "<description of the second player's attack>"
        },
        "summary": "<summary of the game state>"
    }
    """;


    private String gameSummary = "";

    private  HttpEntity<Map<String, Object>> createOpenAiRequest(String fullPrompt, String systemPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", BASE_SYSTEM_MESSAGE + systemPrompt),
                Map.of("role", "user", "content", fullPrompt)
        ));
        requestBody.put("max_tokens", 2000);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        return request;
    }

    private String sendRequest(String fullPrompt, String systemPrompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> request  = createOpenAiRequest(fullPrompt, systemPrompt);

        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        return OpenAiUtility.extractGeneratedText(response.getBody());
    }

    public AiCardsResponse generateCards() throws Exception {
        String fullPrompt = "Generate new cards and environment based on the following game summary: " + gameSummary;
        String jsonResponse = sendRequest(fullPrompt, CARDS_PROMPT);
        AiCardsResponse cardResponse = OpenAiUtility.extractCardResponse(jsonResponse);
        gameSummary = cardResponse.getSummary();
        return cardResponse;
    }

    public AiEvaluationsResponse evaluateResponses(String prompt) throws Exception {
        String fullPrompt = "Evaluate the player responses based on the following game summary: " + gameSummary + ". " + prompt;
        String jsonResponse = sendRequest(fullPrompt, EVALUATION_PROMPT);
        AiEvaluationsResponse evaluationResponse = OpenAiUtility.extractEvaluationResponse(jsonResponse);
        gameSummary = evaluationResponse.getSummary();
        return evaluationResponse;
    }
}
