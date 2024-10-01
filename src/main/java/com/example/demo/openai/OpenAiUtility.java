package com.example.demo.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiUtility {
    public static String extractGeneratedText(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Extract the generated text from choices[0].text
        return root.path("choices").get(0).path("text").asText();
    }
}

