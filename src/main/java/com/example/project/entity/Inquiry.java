package com.example.project.entity;

import java.time.LocalDateTime;

import com.example.project.dto.MemberDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
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

    @Column(name = "inquiry_type")
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

    public InquiryStatus getStatus() {
        return status;
    }

    public void setStatus(InquiryStatus status) {
        this.status = status;
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
            this.username = this.member.getName();
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
