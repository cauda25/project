package com.example.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Member u WHERE u.lastLogin < :lastTime AND u.statusRole = :statusRole")
    List<Member> findInactiveUsers(LocalDateTime lastTime, StatusRole statusRole);

    @Query("SELECT u FROM Member u WHERE u.lastLogin = u.lastLogin AND u.mid = :mid")
    List<Member> findByLastLogin(Long mid);
}