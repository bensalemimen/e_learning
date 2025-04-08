package com.security.EduNext.Repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.security.EduNext.Entities.Certificate;

import java.util.List;


@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // Rechercher les certificats par utilisateur
    List<Certificate> findByUserId(int userId);

    // Rechercher les certificats par examen
    public List<Certificate> findByExam_IdExam(int examId);

    @Query("SELECT c.certificateTitle, COUNT(c.id) as count FROM Certificate c GROUP BY c.certificateTitle ORDER BY count DESC")
    List<Object[]> findTop5Certificates(Pageable pageable);

}
