package com.interviewai.ai.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AnalysisController {

    @PostMapping("/analyze")
    public String analyzeInterview(@RequestBody String text) {

        System.out.println("Procesando texto: " + text);
        return "Análisis IA completado: El candidato muestra gran conocimiento en arquitectura de software. (Simulado)";
    }
}