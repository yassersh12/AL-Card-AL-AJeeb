package com.cotede.interns.task.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OpenAiUtility {
    private final static ObjectMapper objectMapper = new ObjectMapper();


    public static String extractGeneratedText(String jsonResponse) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(jsonResponse);
        // Extract the generated text from choices[0].message.content
        String generatedText = root.path("choices").get(0).path("message").path("content").asText();
        System.out.println(generatedText);
        return generatedText;
    }

    // Method to extract AiCardResponse from a JSON response
    public static AiCardsResponse extractCardResponse(String jsonResponse) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(jsonResponse);

        String card1 = root.path("card1").toString();
        String card2 = root.path("card2").toString();

        String environment = root.path("environment").toString();
        String summary = root.path("summary").toString();

        System.out.println(summary);

        return new AiCardsResponse(card1,card2, environment, summary);
    }

    // Method to extract AiEvaluationsResponse from a JSON response
    public static AiEvaluationsResponse extractEvaluationResponse(String jsonResponse) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Extract userAttack1 and userAttack2 fields from the JSON
        JsonNode userAttack1Node = root.path("userAttack1");
        JsonNode userAttack2Node = root.path("userAttack2");

        Long damage1 = userAttack1Node.path("damage").asLong();
        Long creativity1 = userAttack1Node.path("creativity").asLong();
        String description1 = userAttack1Node.path("description").asText();

        Long damage2 = userAttack2Node.path("damage").asLong();
        Long creativity2 = userAttack2Node.path("creativity").asLong();
        String description2 = userAttack2Node.path("description").asText();

        String summary = root.path("summary").asText();

        AiUserAttack userAttack1 = new AiUserAttack(damage1, creativity1, description1);
        AiUserAttack userAttack2 = new AiUserAttack(damage2, creativity2, description2);
        List<AiUserAttack> aiUserAttacks = new ArrayList<>();
        aiUserAttacks.add(userAttack1);
        aiUserAttacks.add(userAttack2);

        return new AiEvaluationsResponse(aiUserAttacks, summary);
    }

}

