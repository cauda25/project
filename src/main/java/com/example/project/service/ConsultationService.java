package com.example.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.entity.Consultation;
import com.example.project.repository.ConsultationRepository;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public List<Consultation> getConsultationsByUserId(Long userId) {
        return consultationRepository.findByUserId(userId);
    }
}
