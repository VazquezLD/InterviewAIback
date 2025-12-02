package com.interviewai.ai.controller;
import com.interviewai.ai.models.Technology;
import com.interviewai.ai.dto.AnalysisResponse;
import com.interviewai.ai.dto.AnalyzeRequest;
import com.interviewai.ai.dto.QuestionResponse;
import com.interviewai.ai.service.InterviewAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AnalysisController {

    private final InterviewAnalysisService analysisService;

    public AnalysisController(InterviewAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/generate-question")
    public QuestionResponse generateQuestion(
            @RequestParam("tech") Technology tech,
            @RequestParam(value = "difficulty", defaultValue = "Junior") String difficulty
    ) {
        return analysisService.generateQuestion(tech, difficulty);
    }

    @PostMapping("/analyze")
    public AnalysisResponse analyzeInterview(@RequestBody AnalyzeRequest request) {
        return analysisService.analyzeResponse(request.question(), request.answer());
    }
}