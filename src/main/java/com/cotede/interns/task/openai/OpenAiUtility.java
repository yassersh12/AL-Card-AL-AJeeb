package com.cotede.interns.task.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiUtility {
    public static String extractGeneratedText(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Extract the generated text from choices[0].text
        return root.path("choices").get(0).path("text").asText();
    }

    // Method to extract AiCardResponse from a JSON response
    public static AiCardResponse extractCardResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Extract card fields from the JSON
        String object = root.path("object").asText();
        String cardDescription = root.path("card_description").asText();
        String summary = root.path("summary").asText();

        // Create and return the AiCardResponse object
        return new AiCardResponse(object, cardDescription, summary);
    }

    // Method to extract AiEvaluationResponse from a JSON response
    public static AiEvaluationResponse extractEvaluationResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Extract evaluation fields from the JSON
        Long damage1 = root.path("damage1").asLong();
        Long damage2 = root.path("damage2").asLong();
        Long creativity1 = root.path("creativity1").asLong();
        Long creativity2 = root.path("creativity2").asLong();
        String description1 = root.path("description1").asText();
        String description2 = root.path("description2").asText();
        String summary = root.path("summary").asText();

        // Create and return the AiEvaluationResponse object
        return new AiEvaluationResponse(damage1, damage2, creativity1, creativity2, description1, description2, summary);
    }
}

