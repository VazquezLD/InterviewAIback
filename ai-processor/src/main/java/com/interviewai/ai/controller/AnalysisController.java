package com.interviewai.ai.controller;

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
    public String analyzeInterview(@RequestBody String text) {
        System.out.println("🤖 IA Procesando: " + text);
        return analysisService.analyzeResponse(text);
    }
}