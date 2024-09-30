package com.example.demo.openai;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    private final String OPENAI_API_URL = "https://api.openai.com/v1/completions";
    private final String API_KEY = "your-openai-api-key";

    public String getGeneratedResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003"); // Specify which GPT model to use
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Send the request to OpenAI
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        // Extract the generated text from the response
        return response.getBody(); // You would likely parse the response to get just the "text" part
    }

}
