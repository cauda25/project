package com.example.project.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Entity
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String answer;

    @Builder.Default
    private Boolean answered = false; // 기본값 설정

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @Builder.Default
    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "inquiry_type", nullable = false)
    private String inquiryType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Inquiry(String username, String email, String content, String mobile) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.content = content;
    }

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    // Getter와 Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
