package com.interviewai.core.dto;

public record QuestionResponse(
        String question,
        String technology,
        String difficulty
) {}
