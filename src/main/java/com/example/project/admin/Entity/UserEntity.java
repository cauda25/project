package com.example.project.admin.Entity;

import java.time.LocalDateTime;

import com.example.project.admin.Entity.constant.AdminRole;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uno;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String addr;

    @Column(nullable = false)
    private String telNo;

    @Column(nullable = false)
    @Email
    private String email;

    private int point;

    private boolean reser;

    private int gender;

    private String brith;

    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private AdminRole adminRole;

    @Enumerated(EnumType.STRING)
    private StatusRole statusRole;
}
