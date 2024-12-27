package com.example.project.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.ReviewDto;
import com.example.project.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public ResponseEntity<ReviewDto> submitReview(@RequestBody ReviewDto reviewDto) {
        log.info("리뷰: {}", reviewDto);

        // 리뷰 작성 후 해당 영화 페이지로 리다이렉트
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<ReviewDto> getReview(@RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

}