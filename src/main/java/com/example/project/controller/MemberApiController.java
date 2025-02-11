package com.example.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entity.Member;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않았습니다.");
        }
        return ResponseEntity.ok(member);
    }
}