package com.interviewai.core.client;
import com.interviewai.core.dto.AiAnalysisResponse;
import com.interviewai.core.dto.AnalyzeRequest;
import com.interviewai.core.dto.QuestionResponse;
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

    public AiAnalysisResponse analyzeInterview(String question, String answer, String token) {
        AnalyzeRequest requestBody = new AnalyzeRequest(question, answer);
        return restClient.post()
                .uri("/api/ai/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .retrieve()
                .body(AiAnalysisResponse.class);
    }

    public QuestionResponse generateQuestion(String technology, String difficulty, String token) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/ai/generate-question")
                        .queryParam("tech", technology)
                        .queryParam("difficulty", difficulty)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(QuestionResponse.class);
    }
}