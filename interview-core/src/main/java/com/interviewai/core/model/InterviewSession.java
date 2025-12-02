package com.interviewai.core.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    private Technology technology;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private boolean active;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<InterviewExchange> exchanges = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
        this.active = true;
    }
}
