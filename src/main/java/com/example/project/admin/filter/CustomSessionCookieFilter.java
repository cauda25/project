package com.example.project.admin.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSessionCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 처리 전
        filterChain.doFilter(request, response);

        // 응답 처리 후 - 세션 쿠키 이름 변경
        if (request.getRequestURI().startsWith("/admin")) {
            modifySessionCookie(request, response, "ADMIN_SESSION_ID");
        } else if (request.getRequestURI().startsWith("/member")) {
            modifySessionCookie(request, response, "MEMBER_SESSION_ID");
        }
    }

    private void modifySessionCookie(HttpServletRequest request, HttpServletResponse response, String newCookieName) {
        // 기존 JSESSIONID 쿠키 검색
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    // 기존 쿠키 삭제
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    // 새로운 쿠키 추가
                    Cookie newCookie = new Cookie(newCookieName, cookie.getValue());
                    newCookie.setHttpOnly(true);
                    newCookie.setPath("/");
                    newCookie.setMaxAge(60 * 60 * 24 * 14); // 14일
                    response.addCookie(newCookie);
                }
            }
        }
    }

}