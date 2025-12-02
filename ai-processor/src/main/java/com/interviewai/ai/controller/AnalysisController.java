package com.interviewai.ai.controller;

import com.interviewai.ai.dto.AnalysisResponse;
import com.interviewai.ai.service.InterviewAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AnalysisController {

    private final InterviewAnalysisService analysisService;

    public AnalysisController(InterviewAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public AnalysisResponse analyzeInterview(@RequestBody String text) {
        return analysisService.analyzeResponse(text);
    }
}