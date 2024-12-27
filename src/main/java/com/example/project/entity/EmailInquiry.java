package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class EmailInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이메일 문의 ID

    private String name;
    private String email;
    private String content;

    @OneToOne(mappedBy = "emailInquiry")
    private Counseling counseling; // 상담과 1:1 관계

    // 생성자 (선택 사항)
    public EmailInquiry(String name, String email, String content) {
        this.name = name;
        this.email = email;
        this.content = content;
    }

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
