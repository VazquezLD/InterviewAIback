package com.interviewai.core.repository;

import com.interviewai.core.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
    Optional<InterviewSession> findByUserIdAndActiveTrue(String userId);
    List<InterviewSession> findByUserId(String userId);
}