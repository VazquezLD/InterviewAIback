package com.interviewai.core.controller;

import com.interviewai.core.dto.*;
import com.interviewai.core.model.InterviewSession;
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
    @PostMapping("/start")
    public ChatStepResponse startInterview(@RequestBody StartInterviewRequest request, @AuthenticationPrincipal Jwt jwt) {
        return interviewService.startSession(
                jwt.getSubject(),
                request.technology(),
                request.difficulty(),
                jwt.getTokenValue()
        );
    }

    @PostMapping("/reply")
    public ChatStepResponse replyInterview(@RequestBody UserReplyRequest request, @AuthenticationPrincipal Jwt jwt) {
        return interviewService.processReply(
                jwt.getSubject(),
                request.sessionId(),
                request.answer(),
                jwt.getTokenValue()
        );
    }

    @GetMapping("/history")
    public List<InterviewSession> getMySessions(@AuthenticationPrincipal Jwt jwt) {
        return interviewService.getUserSessions(jwt.getSubject());
    }
}
