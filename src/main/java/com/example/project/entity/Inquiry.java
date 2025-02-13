package com.example.project.entity;

import java.time.LocalDateTime;

import com.example.project.dto.MemberDto;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String answer;

    @Builder.Default
    private Boolean answered = false;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @Builder.Default
    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "inquiry_type", nullable = false)
    private String inquiryType;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    public Inquiry(Member member, String email, String content, String phone) {
        this.member = member;
        this.name = member.getName(); // Member의 name 사용
        this.email = email;
        this.phone = phone;
        this.content = content;
        setUsernameFromMember();
    }

    public Member convertToMemberEntity(MemberDto memberDto) {
        Member member = new Member();
        member.setMid(memberDto.getMid());
        return member;
    }

    // Getter와 Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberEmail() {
        return member != null ? member.getEmail() : "알 수 없음";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setUsernameFromMember() {
        if (this.member != null) {
            this.username = this.member.getUsername();
        }
    }
}
