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

    // Store the initial system message and conversation history
    private final List<Map<String, String>> chatHistory = new ArrayList<>();

    public OpenAiService() {
        // System message that will be sent once during initialization
        chatHistory.add(Map.of(
                "role", "system",
                "content", "You are an AI assistant designed to manage a game where two players compete by sending descriptions of how their object/creature would defeat their opponent's. Each round, you generate two random cards, each containing a description of a random object or creature and its opposing object or creature from the other card. You also generate a random environment for the battle. After each player submits their strategy, you evaluate their responses, assigning a damage output between 0 and 50 based on how effective their strategy is, with 50 representing the best possible attack. Both players start with 100 HP, and the game continues in rounds until one player’s HP reaches 0. If both reach 0 HP in the same round, the player with the higher remaining HP (even if negative) wins. At the end of the game, you provide a special creativity grade that assesses the players' creativity throughout the game. You must ensure that the game runs fairly, accurately evaluates the responses, and provides concise feedback for each round."
        ));
    }

    public String getGeneratedResponse(String prompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Add the user's message to the chat history
        chatHistory.add(Map.of("role", "user", "content", prompt));

        // Create the request body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", chatHistory);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Send the request to OpenAI
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        // Extract the generated text from the response using the utility parser
        String generatedText = OpenAiUtility.extractGeneratedText(response.getBody());

        // Add the assistant's response to the chat history for continuity
        chatHistory.add(Map.of("role", "assistant", "content", generatedText));

        return generatedText;
    }
}
