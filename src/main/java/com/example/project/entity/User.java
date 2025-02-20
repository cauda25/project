package com.example.project.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String mobile;

    @Column(nullable = true) // 이름 필드 추가 (nullable 설정은 상황에 맞게 변경 가능)
    private String name;

    // cascade = CascadeType.ALL: User 엔티티가 저장/삭제될 때 관련된 UserInquiry 엔티티에도 동일 작업이
    // 적용됨
    // orphanRemoval = true: UserInquiry 엔티티가 User 엔티티에서 분리될 때 해당 UserInquiry도 자동
    // 삭제됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInquiry> userInquiries = new ArrayList<>();

    public User() {
    }

    // 파라미터를 받는 생성자(username과 email을 초기화 용도)
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<UserInquiry> getUserInquiries() {
        return userInquiries;
    }

    public void setUserInquiries(List<UserInquiry> userInquiries) {
        this.userInquiries = userInquiries;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // 새로 추가된 name 필드의 Getter와 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 사용자와 사용자 문의 간의 관계 결정하는 메소드(양방향 연관관계 설정)
    public void addUserInquiry(UserInquiry inquiry) {
        this.userInquiries.add(inquiry);
        inquiry.setUser(this);
    }

    // 사용자와 사용자 문의 관계를 끊는 메소드
    public void removeUserInquiry(UserInquiry inquiry) {
        this.userInquiries.remove(inquiry);
        inquiry.setUser(null);
    }
}
