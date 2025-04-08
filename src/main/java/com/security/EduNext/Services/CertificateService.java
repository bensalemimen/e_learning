package com.security.EduNext.Services;

import com.security.EduNext.Entities.Certificate;
import com.security.EduNext.Entities.Exam;
import com.security.EduNext.Repositories.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public Optional<Certificate> getCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    public Certificate updateCertificate(Long id, Certificate newDetails) {
        return certificateRepository.findById(id)
                .map(cert -> {
                    cert.setIssuedDate(newDetails.getIssuedDate());
                    cert.setUserFullName(newDetails.getUserFullName());
                    cert.setCertificateTitle(newDetails.getCertificateTitle());
                    return certificateRepository.save(cert);
                })
                .orElseThrow(() -> new RuntimeException("Certificat non trouvé"));
    }

    public List<Map<String, Object>> getTop5Certificates() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Object[]> results = certificateRepository.findTop5Certificates(pageable);

        return results.stream().map(obj -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", obj[0]);
            map.put("count", obj[1]);
            return map;
        }).collect(Collectors.toList());
    }
}

