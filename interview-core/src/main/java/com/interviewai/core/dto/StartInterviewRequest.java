package com.interviewai.core.dto;
import com.interviewai.core.model.Technology;

public record StartInterviewRequest(Technology technology, String difficulty) {}
