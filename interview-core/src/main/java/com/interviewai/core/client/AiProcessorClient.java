package com.interviewai.core.client;

import com.interviewai.core.dto.AiAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiProcessorClient {

    private final RestClient restClient;

    public AiProcessorClient(@Value("${ai-processor.url:http://localhost:8082}") String aiServiceUrl) {
        this.restClient = RestClient.builder().baseUrl(aiServiceUrl).build();
    }

    public AiAnalysisResponse analyzeInterview(String text, String token) {
        return restClient.post()
                .uri("/api/ai/analyze")
                .contentType(MediaType.TEXT_PLAIN)
                .header("Authorization", "Bearer " + token)
                .body(text)
                .retrieve()
                .body(AiAnalysisResponse.class);
    }
}