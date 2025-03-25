package com.blog.service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.url}")
    private String apiUrl;

    public String summarizeBlog(String blogContent) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
            Map.of("parts", List.of(
                Map.of("text", "Summarize this blog: " + blogContent)
            ))
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String fullApiUrl = apiUrl + "?key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(fullApiUrl, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            // Extract summary from response
            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    if (firstCandidate.containsKey("content")) {
                        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                        List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
                        if (!parts.isEmpty()) {
                            return parts.get(0).get("text");
                        }
                    }
                }
            }
            return "Summary not generated.";
        } catch (Exception e) {
            logger.error("Error calling Gemini API:", e); 
            return "Error calling Gemini API: " + e.getMessage();
        }
    }
}