package com.example.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ConsultationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation; // 상담 엔티티와 연결

    @Column(length = 1000)
    private String answerContent; // 답변 내용

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status; // 답변 상태

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

}
