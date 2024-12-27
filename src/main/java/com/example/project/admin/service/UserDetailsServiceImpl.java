package com.example.project.admin.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.admin.Entity.Admin;
import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.dto.test.AuthUserDto;
import com.example.project.admin.dto.test.UserDto;
import com.example.project.admin.repository.AdminRepository;
import com.example.project.admin.repository.UserRepository;
import com.example.project.admin.service.test.UserServie;
import com.example.project.dto.AdminDto;
import com.example.project.dto.AuthAdminDto;
import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.entity.constant.MemberRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Service username : {}", username);

        Optional<UserEntity> result = userRepository.findByUserId(username);

        // 정보 없을 시 Exception 던지기
        if (!result.isPresent()) {
            throw new UsernameNotFoundException("아이디나 비밀번호 확인");
        }

        UserEntity userEntity = result.get();

        // 최종 로그인 날짜 수정
        accessLogin(userEntity);

        // INACTIVE 상태인 경우 예외 처리
        if (userEntity.getStatusRole() == StatusRole.INACTIVE) {
            throw new IllegalStateException("Inactive accounts cannot login. Please contact support.");
        }

        log.info("userEntity active {}", userEntity);

        // Spring Security의 UserDetails 구현체 반환
        MemberDto memberDto = MemberDto.builder()
                .mid(userEntity.getUno())
                .memberId(userEntity.getUserId())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .lastLogin(userEntity.getLastLogin())
                .role(MemberRole.MEMBER)
                .statusRole(userEntity.getStatusRole())
                .build();

        return new AuthMemberDto(memberDto);

    }

    private void accessLogin(UserEntity userEntity) {

        userEntity.setLastLogin(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    // private ResponseEntity<String> login(String username) {
    // Optional<UserEntity> userOptional = userRepository.findByUserId(username);

    // if (userOptional.isEmpty()) {
    // return ResponseEntity.status(404).body("User not found");
    // }

    // UserEntity user = userOptional.get();

    // // INACTIVE 상태인 경우 로그인 제한
    // if (user.getStatusRole() == StatusRole.INACTIVE) {
    // return ResponseEntity.status(403).body("Your account is inactive. Please
    // contact support.");
    // }

    // return ResponseEntity.ok("Login successful");
    // }

}
