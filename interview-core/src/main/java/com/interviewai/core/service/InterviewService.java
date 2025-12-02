package com.interviewai.core.service;

import com.interviewai.core.client.AiProcessorClient;
import com.interviewai.core.dto.*;
import com.interviewai.core.model.*;
import com.interviewai.core.repository.InterviewSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewSessionRepository sessionRepository;
    private final AiProcessorClient aiClient;

    @Transactional
    public ChatStepResponse startSession(String userId, Technology tech, String difficulty, String token) {
        InterviewSession session = new InterviewSession();
        session.setUserId(userId);
        session.setTechnology(tech);
        session.setActive(true);
        QuestionResponse aiQuestion = aiClient.generateQuestion(tech.name(), difficulty, token);
        InterviewExchange exchange = new InterviewExchange();
        exchange.setSession(session);
        exchange.setQuestion(aiQuestion.question());
        session.getExchanges().add(exchange);
        sessionRepository.save(session);

        return new ChatStepResponse(
                session.getId(),
                exchange.getId(),
                exchange.getQuestion(),
                null,
                null,
                "ACTIVE"
        );
    }


    @Transactional
    public ChatStepResponse processReply(String userId, Long sessionId, String answer, String token) {
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        if (!session.getUserId().equals(userId)) throw new RuntimeException("No autorizado");
        if (!session.isActive()) throw new RuntimeException("La entrevista ya finalizó");

        int lastIdx = session.getExchanges().size() - 1;
        InterviewExchange currentExchange = session.getExchanges().get(lastIdx);

        currentExchange.setCandidateAnswer(answer);
        AiAnalysisResponse analysis;
        try {
            analysis = aiClient.analyzeInterview(currentExchange.getQuestion(), answer, token);
            currentExchange.setAiFeedback(analysis.feedback());
            currentExchange.setScore(analysis.nota());
        } catch (Exception e) {
            throw new RuntimeException("Error al analizar respuesta: " + e.getMessage());
        }

        String nextQuestionText = null;
        Long nextExchangeId = null;

        try {
            QuestionResponse nextAiQuestion = aiClient.generateQuestion(
                    session.getTechnology().name(),
                    "Senior", // TODO: Usar dificultad de la sesión, no hardcodeada
                    token
            );
            InterviewExchange nextExchange = new InterviewExchange();
            nextExchange.setSession(session);
            nextExchange.setQuestion(nextAiQuestion.question());
            session.getExchanges().add(nextExchange);
            sessionRepository.save(session);
            nextExchangeId = session.getExchanges().get(session.getExchanges().size() - 1).getId();
            nextQuestionText = nextAiQuestion.question();

        } catch (Exception e) {
            System.err.println("Alerta: No se pudo generar la siguiente pregunta. " + e.getMessage());
            sessionRepository.save(session);
        }

        return new ChatStepResponse(
                session.getId(),
                nextExchangeId,
                nextQuestionText,
                analysis.feedback(),
                analysis.nota(),
                "ACTIVE"
        );
    }

    public List<InterviewSession> getUserSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }
}
