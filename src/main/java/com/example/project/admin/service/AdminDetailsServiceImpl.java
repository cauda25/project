package com.example.project.admin.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.Admin;
import com.example.project.admin.dto.test.AdminDto;
import com.example.project.admin.dto.test.AuthAdminDto;
import com.example.project.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Primary
@RequiredArgsConstructor
@Log4j2
@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Service username : {}", username);

        Optional<Admin> result = adminRepository.findByUserId(username);

        // 정보 없을 시 Exception 던지기
        if (!result.isPresent()) {
            throw new UsernameNotFoundException("아이디 확인");
        }

        Admin admin = result.get();

        // buildAdminAuthority();

        AdminDto adminDto = AdminDto.builder()
                .ano(admin.getAno())
                .userId(admin.getUserId())
                .password(admin.getPassword())
                .role(admin.getRole())
                .build();
        return new AuthAdminDto(adminDto);

    }

    // private Set<GrantedAuthority> buildAdminAuthority() {
    // Set<GrantedAuthority> setAuth = new HashSet<GrantedAuthority>();

    // setAuth.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

    // return setAuth;
    // }

}
