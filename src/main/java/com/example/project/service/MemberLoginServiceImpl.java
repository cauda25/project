package com.example.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.Admin;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.dto.test.AdminDto;
import com.example.project.admin.dto.test.AuthAdminDto;
import com.example.project.admin.repository.AdminRepository;
import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.MemberRole;
import com.example.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// @Primary
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberLoginServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Authenticating username: {}", username);

        Optional<Admin> adminResult = adminRepository.findByUserId(username);
        if (adminResult.isPresent()) {
            Admin admin = adminResult.get();
            log.info("Admin user authenticated: {}", admin);

            return new AuthMemberDto(
                    MemberDto.builder()
                            .memberId(admin.getUserId())
                            .password(admin.getPassword())
                            .role(admin.getRole())
                            .build());

        }

        // 사용자 조회
        Optional<Member> result = memberRepository.findByMemberId(username);

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        Member member = result.get();

        // 로그인 날짜 업데이트
        accessLogin(member);

        // 사용자 상태 확인
        if (member.getStatusRole() == StatusRole.INACTIVE) {
            throw new IllegalStateException("Account is inactive. Please contact support.");
        }

        log.info("Authenticated user: {}", member);

        // 사용자 정보를 DTO로 변환
        MemberDto memberDto = MemberDto.builder()
                .mid(member.getMid())
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .gender(member.getGender())
                .birth(member.getBirth())
                .phone(member.getPhone())
                .city(member.getCity())
                .district(member.getDistrict())
                .point(member.getPoint())
                .role(member.getRole())
                .statusRole(member.getStatusRole())
                .build();

        // AuthMemberDto로 변환하여 반환
        return new AuthMemberDto(memberDto);

    }

    // 로그인 날짜 업데이트 메서드
    private void accessLogin(Member member) {
        member.setLastLogin(LocalDateTime.now());
        memberRepository.save(member);
    }
}