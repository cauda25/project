package com.example.project.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.ReviewDto;
import com.example.project.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // private final MemberService memberService;

    // @PostMapping("/submit")
    // public String submitReview(@RequestBody Long memberId,
    // @RequestBody Long movieId) {
    // // memberId와 movieId가 null인 경우 예외 처리
    // log.info("영화id: {}", movieId);
    // log.info("멤버id: {}", memberId);

    // // if (reviewDto.getMemberId() == null || reviewDto.getMovieId() == null) {
    // // throw new IllegalArgumentException("MovieId or MemberId cannot be null");
    // // }

    // // // 디버그 로그
    // // System.out.println("Review Submitted: " + reviewDto);

    // // // 서비스 호출 (리뷰 저장)
    // // reviewService.saveReview(reviewDto);

    // // return "redirect:/movieDetail?id=" + reviewDto.getMovieId(); // 상세 페이지로
    // 리다이렉트
    // return "movieDetail";
    // }

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