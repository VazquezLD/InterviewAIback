package com.interviewai.core.controller;

import com.interviewai.core.client.AiProcessorClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/core/test")
public class TestController {

    private final AiProcessorClient aiClient;

    public TestController(AiProcessorClient aiClient) {
        this.aiClient = aiClient;
    }

    @GetMapping
    public String testSeguridad(@AuthenticationPrincipal Jwt jwt) {
        return "¡Hola! Tu ID es: " + jwt.getSubject();
    }


    @PostMapping("/analyze")
    public String analyzeWithAi(@RequestBody String interviewText, @AuthenticationPrincipal Jwt jwt) {
        String tokenValue = jwt.getTokenValue();
        return aiClient.analyzeInterview(interviewText, tokenValue);
    }
}