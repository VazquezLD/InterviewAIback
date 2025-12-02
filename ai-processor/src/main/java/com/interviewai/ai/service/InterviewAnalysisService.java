package com.interviewai.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.ai.models.Technology;
import com.interviewai.ai.dto.AnalysisResponse;
import com.interviewai.ai.dto.QuestionResponse;
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

    public QuestionResponse generateQuestion(Technology technology, String level) {

        String jsonExample = """
        {
            "question": "El texto de la pregunta aquí",
            "technology": "JAVA",
            "difficulty": "Senior"
        }
        """;

        String template = """
                Actúa como un Entrevistador Técnico experto en {tech}.
                Genera UNA sola pregunta de entrevista teórica o práctica para un nivel {level}.
                
                Responde ÚNICAMENTE con un objeto JSON válido.
                NO escribas introducción, NO uses markdown, SOLO el JSON crudo.
                Estructura requerida:
                {json_example}
                """;

        String rawResponse = chatClient.prompt()
                .user(u -> u.text(template)
                        .param("tech", technology.name())
                        .param("level", level)
                        .param("json_example", jsonExample))
                .call()
                .content();

        System.out.println("🤖 Pregunta Generada (Cruda): " + rawResponse);
        return parseJson(rawResponse, QuestionResponse.class);
    }

    public AnalysisResponse analyzeResponse(String question, String candidateAnswer) {

        String jsonExample = """
        {
            "esCorrecta": true,
            "nivelDetectado": "Senior",
            "feedback": "Texto breve",
            "nota": 90
        }
        """;

        String template = """
                Actúa como un Entrevistador Técnico Senior muy exigente.
                
                CONTEXTO:
                Pregunta original: "{question}"
                Respuesta del candidato: "{answer}"
                
                Tu tarea:
                1. Evalúa si la respuesta es correcta para ESA pregunta específica.
                2. Evalúa la profundidad técnica.
                
                Responde ÚNICAMENTE con un objeto JSON válido.
                NO escribas introducción, NO uses markdown, SOLO el JSON crudo.
                Estructura requerida:
                {json_example}
                """;

        String rawResponse = chatClient.prompt()
                .user(u -> u.text(template)
                        .param("question", question) // Inyectamos la pregunta
                        .param("answer", candidateAnswer) // Inyectamos la respuesta
                        .param("json_example", jsonExample)) // Inyectamos el formato JSON
                .call()
                .content();

        System.out.println("🤖 Respuesta Cruda de IA: " + rawResponse);
        return parseJson(rawResponse, AnalysisResponse.class);
    }

    private <T> T parseJson(String rawResponse, Class<T> targetClass) {
        String jsonClean = rawResponse
                .replace("```json", "")
                .replace("```", "")
                .trim();

        try {
            return objectMapper.readValue(jsonClean, targetClass);
        } catch (Exception e) {
            System.err.println("Error parseando JSON de IA: " + e.getMessage());
            throw new RuntimeException("Error al procesar respuesta de IA", e);
        }
    }
}
