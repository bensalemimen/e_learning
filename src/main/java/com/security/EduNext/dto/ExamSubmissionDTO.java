package com.security.EduNext.dto;


import com.security.EduNext.Entities.Exam;
import com.security.EduNext.Entities.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity

public class ExamSubmissionDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Exam exam;


    @ElementCollection
    @CollectionTable(name = "user_answers", joinColumns = @JoinColumn(name = "submission_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer")
    private Map<Long, String> userAnswers;  // ID de la question -> Réponse de l'utilisateur

    private int score;
    private LocalDateTime submissionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Map<Long, String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(Map<Long, String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    // Getters et setters
}



