package com.example.project.admin.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // UserEntity user = (UserEntity) authentication.getPrincipal();
        // log.info("로그인 성공 후 {}", user.getStatusRole());

        // ACTIVE 상태면 기본 성공 URL로 이동
        response.sendRedirect("/admin/page/index");

    }
}
