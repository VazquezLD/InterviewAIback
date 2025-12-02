package com.interviewai.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.ai.dto.AnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class InterviewAnalysisService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public InterviewAnalysisService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public AnalysisResponse analyzeResponse(String candidateAnswer) {

        String template = """
                Actúa como un Entrevistador Técnico Senior muy exigente.
                Analiza esta respuesta técnica de un candidato:
                "%s"
                
                Responde ÚNICAMENTE con un objeto JSON válido.
                NO escribas introducción, NO uses markdown, SOLO el JSON crudo.
                Estructura requerida:
                \\{
                    "esCorrecta": boolean,
                    "nivelDetectado": "Junior" | "Mid" | "Senior",
                    "feedback": "Texto breve",
                    "nota": numero_entero_1_al_100
                \\}
                """.formatted(candidateAnswer);

        String rawResponse = chatClient.prompt()
                .user(template)
                .call()
                .content();

        System.out.println("🤖 Respuesta Cruda de IA: " + rawResponse);


        String jsonClean = rawResponse
                .replace("```json", "")
                .replace("```", "")
                .trim();

        try {
            return objectMapper.readValue(jsonClean, AnalysisResponse.class);
        } catch (Exception e) {
            System.err.println("Error parseando JSON de IA: " + e.getMessage());

            return new AnalysisResponse(false, "Error", "Error interno al procesar la respuesta de la IA.", 0);
        }
    }
}
