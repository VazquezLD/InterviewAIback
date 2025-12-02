package com.interviewai.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InterviewAnalysisService {

    private final ChatClient chatClient;

    public InterviewAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String analyzeResponse(String candidateAnswer) {
        String template = """
                Actúa como un Arquitecto de Software Senior muy exigente.
                Estás entrevistando a un candidato para un puesto Senior.
                
                Analiza esta respuesta técnica:
                "%s"
                
                Tu tarea:
                1. Detecta si hay imprecisiones técnicas.
                2. Evalúa la profundidad del conocimiento.
                3. Responde ÚNICAMENTE con un objeto JSON con este formato (sin markdown):
                {
                    "esCorrecta": boolean,
                    "nivelDetectado": "Junior" | "Mid" | "Senior",
                    "feedback": "Texto breve",
                    "nota": numero_entero_1_al_100
                }
                """.formatted(candidateAnswer);

        return chatClient.prompt()
                .user(template)
                .call()
                .content();
    }
}
