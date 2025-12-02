package com.interviewai.core.service;

import com.interviewai.core.client.AiProcessorClient;
import com.interviewai.core.model.Interview;
import com.interviewai.core.model.InterviewStatus;
import com.interviewai.core.dto.AiAnalysisResponse;
import com.interviewai.core.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final AiProcessorClient aiClient;

    @Transactional
    public Interview processInterview(String userId, String answer, String token) {
        Interview interview = Interview.builder()
                .userId(userId)
                .candidateAnswer(answer)
                .status(InterviewStatus.PENDING)
                .build();

        interview = interviewRepository.save(interview);

        try {
            AiAnalysisResponse analysis = aiClient.analyzeInterview(answer, token);
            interview.setAiFeedback(analysis.feedback());
            interview.setScore(analysis.nota());
            interview.setStatus(InterviewStatus.COMPLETED);

        } catch (Exception e) {
            System.err.println("Error en IA: " + e.getMessage());
            interview.setStatus(InterviewStatus.FAILED);
            interview.setAiFeedback("Error al procesar: " + e.getMessage());
        }
        return interviewRepository.save(interview);
    }

    public List<Interview> getUserHistory(String userId) {
        return interviewRepository.findByUserId(userId);
    }
}
