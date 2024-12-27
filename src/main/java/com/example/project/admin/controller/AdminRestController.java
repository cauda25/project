package com.example.project.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/admin")
public class AdminRestController {

    @DeleteMapping("/page/{theaterId}")
    public Long getMethodName(@PathVariable Long theaterId) {
        return theaterId;
    }

}
