package com.example.project.admin.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.repository.UserRepository;
import com.example.project.admin.service.test.UserServie;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/admin")
public class AdminLoginController {

    @GetMapping("/login")
    public void getHome() {
        log.info("home 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthentication() {

        // SecurityContext : 인증된 유저를 저장하는 곳
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;
    }
}
