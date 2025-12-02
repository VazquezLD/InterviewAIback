package com.interviewai.core.controller;
import com.interviewai.core.model.Interview;
import com.interviewai.core.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/core/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;


    @PostMapping
    public Interview submitInterview(@RequestBody String answer, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        String token = jwt.getTokenValue();

        return interviewService.processInterview(userId, answer, token);
    }

    @GetMapping
    public List<Interview> getMyHistory(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return interviewService.getUserHistory(userId);
    }
}
