package com.interviewai.ai.dto;

public record AnalysisResponse(
        boolean esCorrecta,
        String nivelDetectado,
        String feedback,
        Integer nota
) {}
