package com.example.project.controller;

import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.Review;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import com.example.project.dto.MemberDto;
import com.example.project.dto.ReviewDto;
import com.example.project.repository.MemberRepository;
import com.example.project.service.MemberService;
import com.example.project.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        // 리뷰 데이터를 가져오는 로직
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody ReviewDto reviewDto) {
        try {
            reviewService.saveReview(reviewDto);
            return ResponseEntity.ok("리뷰가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 저장 중 오류가 발생했습니다.");
        }
    }

    // 특정 영화에 대한 리뷰 조회
    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovie(@PathVariable Long id) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(id);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 리뷰 ID로 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long reviewId) {
        // 리뷰를 가져오는 서비스 로직 필요 (getReviewById 메서드 추가해야 함)
        ReviewDto review = reviewService.getReviewById(reviewId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }
}