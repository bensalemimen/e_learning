package com.security.EduNext.Repositories;




import com.security.EduNext.Entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findByExamTitleContainingIgnoreCase(String examTitle);
}
