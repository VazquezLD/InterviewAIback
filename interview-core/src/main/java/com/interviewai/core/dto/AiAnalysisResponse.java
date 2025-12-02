package com.interviewai.core.dto;

public record AiAnalysisResponse(
        boolean esCorrecta,
        String nivelDetectado,
        String feedback,
        Integer nota
) {}
