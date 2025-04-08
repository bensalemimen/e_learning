package com.security.EduNext.Repositories;

import com.security.EduNext.Entities.Exam;
import com.security.EduNext.Entities.User;
import com.security.EduNext.dto.ExamSubmissionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmissionDTO,Long> {
    Optional<ExamSubmissionDTO> findByUserAndExam(User user, Exam exam);
    List<ExamSubmissionDTO> findByUserId(Long userId);

    // Trouver la soumission d'un utilisateur pour un examen spécifique
    ExamSubmissionDTO findByUserIdAndExam_IdExam(Long userId, int examId);

    @Query("SELECT e FROM ExamSubmissionDTO e WHERE e.score = (SELECT MAX(es.score) FROM ExamSubmissionDTO es WHERE es.exam = e.exam)ORDER BY e.score DESC")
    List<ExamSubmissionDTO> findTop10ByOrderByScoreDesc();



}
