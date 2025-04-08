package com.security.EduNext.Controllers;



import com.security.EduNext.Entities.Certificate;
import com.security.EduNext.Repositories.CertificateRepository;
import com.security.EduNext.Services.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/api/certificates")

public class CertificateController {
    @Autowired

    private final CertificateRepository certificateRepository;

    @Autowired
    private CertificateService certificateService;

    // Récupérer tous les certificats
    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        List<Certificate> certificates = certificateRepository.findAll();
        return ResponseEntity.ok(certificates);
    }
    @GetMapping("/certificates/{id}")
    public Optional<Certificate> getCertificateById(@PathVariable Long id) {
        return certificateService.getCertificateById(id);
    }

    // Récupérer les certificats par utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Certificate>> getCertificatesByUser(@PathVariable int userId) {
        List<Certificate> certificates = certificateRepository.findByUserId(userId);
        return ResponseEntity.ok(certificates);
    }

    // Récupérer les certificats par examen
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<Certificate>> getCertificatesByExam(@PathVariable int examId) {
        List<Certificate> certificates = certificateRepository.findByExam_IdExam(examId);
        return ResponseEntity.ok(certificates);
    }

    // Créer un certificat manuellement (optionnel, normalement cela se fait via la soumission de l'examen)
    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) {
        Certificate createdCertificate = certificateRepository.save(certificate);
        return ResponseEntity.ok(createdCertificate);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Certificate> updateCertificate(@PathVariable Long id, @RequestBody Certificate certificateDetails) {
        System.out.println("🔍 Requête reçue pour mettre à jour le certificat ID: " + id);
        Certificate updatedCertificate = certificateService.updateCertificate(id, certificateDetails);
        return ResponseEntity.ok(updatedCertificate);
    }



    // Supprimer un certificat
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long certificateId) {
        certificateRepository.deleteById(certificateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Map<String, Object>>> getTop10Certificates() {
        return ResponseEntity.ok(certificateService.getTop5Certificates());
    }
}
