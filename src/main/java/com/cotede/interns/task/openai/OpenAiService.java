package com.cotede.interns.task.openai;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final String API_KEY = "your-openai-api-key";

    // Predefined system messages for different game operations
    private final String GENERATE_CARDS_INSTRUCTION =
            "Generate two random cards: each containing a description of " +
            "a random object or creature, and its opposing object or creature (from the other card). " +
            "Also, generate a random environment for the battle.";
    private final String EVALUATE_RESPONSES_INSTRUCTION =
            "Evaluate the strategies of two players based on the following " +
            "descriptions. For each player, assign a damage output between 0 and 50 based on their effectiveness" +
            " and a creativity score out of 100 based on how creative their response is.";

    // Store the system message and conversation history
    private final List<Map<String, String>> chatHistory = new ArrayList<>();

    public OpenAiService() {

        String systemMessage =
                "You are an AI assistant managing a game where two players battle with random creatures/objects." +
                " You generate cards, evaluate their strategies, assign damage and creativity scores, and provide feedback.";
        chatHistory.add(Map.of("role", "system", "content", systemMessage));
    }

    private String executeOpenAiRequest(String fullPrompt) throws Exception {
        chatHistory.add(Map.of("role", "user", "content", fullPrompt));

        HttpEntity<Map<String, Object>> request = setupOpenAiRequest(fullPrompt);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        String generatedText = OpenAiUtility.extractGeneratedText(response.getBody());

        // Add assistant's response to chat history for future continuity
        chatHistory.add(Map.of("role", "assistant", "content", generatedText));

        return generatedText;
    }

    private HttpEntity<Map<String, Object>> setupOpenAiRequest(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", chatHistory);
        requestBody.put("max_tokens", 150);

        return new HttpEntity<>(requestBody, headers);
    }

    public String generateCards(String prompt) throws Exception {
        String fullPrompt = GENERATE_CARDS_INSTRUCTION + " " + prompt;
        return executeOpenAiRequest(fullPrompt);
    }

    public String evaluateResponses(String prompt) throws Exception {
        String fullPrompt = EVALUATE_RESPONSES_INSTRUCTION + " " + prompt;
        return executeOpenAiRequest(fullPrompt);
    }
}
