package com.example.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "Counseling")
@Entity
public class Counseling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 경우 AUTO_INCREMENT 설정
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "NAME")
    private String name; // 이름

    @Column(name = "EMAIL")
    private String email; // 이메일

    @Column(name = "COUNSELING_TYPE")
    private String counselingType; // 상담 유형

    @Column(name = "CONTENT")
    private String content; // 문의 내용

    @Column(name = "STATUS")
    private String status; // 상태

    // Getter와 Setter 메서드 추가
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCounselingType() {
        return counselingType;
    }

    public void setCounselingType(String counselingType) {
        this.counselingType = counselingType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // emailInquiry와의 관계
    @OneToOne
    @JoinColumn(name = "email_inquiry_id") // 외래 키
    private EmailInquiry emailInquiry;

    // Getter와 Setter for emailInquiry
    public EmailInquiry getEmailInquiry() {
        return emailInquiry;
    }

    public void setEmailInquiry(EmailInquiry emailInquiry) {
        this.emailInquiry = emailInquiry;
    }
}
