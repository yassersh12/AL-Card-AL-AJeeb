package com.cotede.interns.task.card;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardUtility {
    private final static ObjectMapper mapper = new ObjectMapper();
    private static String objectTag = "object";
    private static String descriptionTag = "cardDescription";

    public static Card extractCard(String cardJsonResponse) throws JsonProcessingException {
        JsonNode root = mapper.readTree(cardJsonResponse);

        String object = root.path(objectTag).asText();
        String cardDescription = root.path(descriptionTag).asText();
        return Card.builder()
                .object(object)
                .cardDescription(cardDescription)
                .build();
    }
}
