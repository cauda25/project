// package com.example.project.admin.service;

// import java.util.Optional;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.project.admin.Entity.Admin;
// import com.example.project.admin.repository.AdminRepository;
// import com.example.project.dto.AdminDto;
// import com.example.project.dto.AuthAdminDto;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.log4j.Log4j2;

// @RequiredArgsConstructor
// @Log4j2
// @Service
// public class AdminDetailsServiceImpl implements UserDetailsService {

// private final AdminRepository adminRepository;

// @Override
// public UserDetails loadUserByUsername(String username) throws
// UsernameNotFoundException {
// log.info("Service username : {}", username);

// Optional<Admin> result = adminRepository.findByUserId(username);

// // 정보 없을 시 Exception 던지기
// if (!result.isPresent()) {
// throw new UsernameNotFoundException("아이디 확인");
// }

// Admin admin = result.get();

// AdminDto adminDto = AdminDto.builder()
// .ano(admin.getAno())
// .userId(admin.getUserId())
// .password(admin.getPassword())
// .role(admin.getAdminRole())
// .build();
// return new AuthAdminDto(adminDto);

// }

// }
