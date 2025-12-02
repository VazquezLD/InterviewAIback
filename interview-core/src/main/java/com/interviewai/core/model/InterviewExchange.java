package com.interviewai.core.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_exchanges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String candidateAnswer;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private Integer score;
}
