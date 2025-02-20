package com.example.project.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    // 생성자 주입
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 현재 로그인한 사용자를 반환하는 메소드
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // username을 기반으로 UserRepository에서 사용자 정보를 조회
        return userRepository.findByUsername(username);
        // .orElseThrow(() -> new RuntimeException("User not found with username: " +
        // username));
    }
}
