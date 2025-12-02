package com.interviewai.core.dto;

public record ChatStepResponse(
        Long sessionId,
        Long exchangeId,
        String question,
        String feedback,
        Integer score,
        String status
) {}
