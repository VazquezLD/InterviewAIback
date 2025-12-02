package com.interviewai.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AiProcessorApplication {
    public static void main(String[] args) {
        // --- CÓDIGO DE DEBUG (BORRAR LUEGO) ---
        String key = System.getenv("GROQ_API_KEY");
        System.out.println("========================================");
        System.out.println("👀 LA CLAVE QUE VEO ES: " + key);
        System.out.println("========================================");
        // --------------------------------------

        SpringApplication.run(AiProcessorApplication.class, args);
    }
}

