package com.example.project.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInquiry> userInquiries = new ArrayList<>();

    public User() {
    }

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

    public void addUserInquiry(UserInquiry inquiry) {
        this.userInquiries.add(inquiry);
        inquiry.setUser(this);
    }

    public void removeUserInquiry(UserInquiry inquiry) {
        this.userInquiries.remove(inquiry);
        inquiry.setUser(null);
    }
}
