package com.example.project.service;

import com.example.project.entity.Consultation;
import com.example.project.repository.ConsultationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    // Constructor injection
    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    // 사용자 ID에 맞는 상담 목록 조회
    public List<Consultation> getConsultationsByUserId(Long userId) {
        return consultationRepository.findByUserId(userId);
    }

    // 모든 상담 내역 조회
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll(); // 데이터 조회
    }
}
