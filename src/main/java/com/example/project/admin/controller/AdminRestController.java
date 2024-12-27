package com.example.project.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.admin.service.test.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/admin/page")
public class AdminRestController {

    private final UserService userServie;

    @GetMapping("/{theaterId}")
    public Long getMethodName(@PathVariable Long theaterId) {
        return theaterId;
    }

    @DeleteMapping("/{theaterId}")
    public Long postRemove(@PathVariable Long theaterId) {
        log.info("영화관삭제 폼 요청 {} ", theaterId);
        userServie.delete(theaterId);

        return theaterId;
    }
}
