package com.example.project.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.admin.service.test.UserServie;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/admin/page")
public class AdminRestController {

    @GetMapping("/{theaterId}")
    public Long getMethodName(@PathVariable Long theaterId) {
        return theaterId;
    }

}
