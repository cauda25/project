package com.example.project.admin.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            throw new UsernameNotFoundException("아이디 확인");
        }

        UserEntity userEntity = result.get();

        // 최종 로그인 날짜 수정
        accessLogin(userEntity);

        UserDto userDto = UserDto.builder()
                .uno(userEntity.getUno())
                .userId(userEntity.getUserId())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .telNo(userEntity.getTelNo())
                .email(userEntity.getEmail())
                .lastLogin(userEntity.getLastLogin())
                .adminRole(userEntity.getAdminRole())
                .statusRole(userEntity.getStatusRole())
                .build();
        return new AuthUserDto(userDto);
    }

    private void accessLogin(UserEntity userEntity) {

        userEntity.setLastLogin(LocalDateTime.now());
        userRepository.save(userEntity);
    }

}
