package com.cotede.interns.task.environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnvironmentUtility {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static Environment extractEnvironment(String environmentJsonResponse) throws JsonProcessingException {
        JsonNode root = mapper.readTree(environmentJsonResponse);

        String fightingPlace = root.get("fightingPlace").asText();
        String weather = root.get("weather").asText();
        return Environment.builder()
                .fightingPlace(fightingPlace)
                .weather(weather)
                .build();
    }
}
