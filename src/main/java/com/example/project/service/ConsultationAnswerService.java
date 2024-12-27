package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.entity.Consultation;
import com.example.project.entity.ConsultationAnswer;
import com.example.project.entity.ConsultationStatus;
import com.example.project.repository.ConsultationAnswerRepository;
import com.example.project.repository.ConsultationRepository;

import jakarta.transaction.Transactional;

@Service
public class ConsultationAnswerService {

    private final ConsultationAnswerRepository consultationAnswerRepository;
    private final ConsultationRepository consultationRepository;

    @Autowired
    public ConsultationAnswerService(ConsultationAnswerRepository consultationAnswerRepository,
            ConsultationRepository consultationRepository) {
        this.consultationAnswerRepository = consultationAnswerRepository;
        this.consultationRepository = consultationRepository;
    }

    // 상담에 답변 추가
    @Transactional
    public void addAnswer(Long consultationId, String answerContent) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new IllegalArgumentException("상담이 존재하지 않습니다."));

        // 답변 엔티티 생성
        ConsultationAnswer answer = new ConsultationAnswer();
        answer.setConsultation(consultation); // 상담에 대한 연관 설정
        answer.setAnswerContent(answerContent); // 답변 내용 설정

        consultationAnswerRepository.save(answer); // 답변 저장

        // 상담 상태를 '답변 완료'로 변경
        consultation.setStatus(ConsultationStatus.ANSWERED);
        consultationRepository.save(consultation);
    }
}
