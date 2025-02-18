package com.example.project.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.admin.service.test.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/admin")
public class AdminRestController {
    private final UserService userService;

    @GetMapping("/page/movie/{theaterId}")
    public Long getMethodName(@PathVariable Long theaterId) {
        return theaterId;
    }

    @DeleteMapping("/page/movie/{theaterId}")
    public Long getMethodd(@PathVariable Long theaterId) {
        userService.delete(theaterId);
        return theaterId;
    }

}
