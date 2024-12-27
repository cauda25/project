package com.example.project.admin.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.repository.UserRepository;

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
