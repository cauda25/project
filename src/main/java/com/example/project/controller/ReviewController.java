package com.example.project.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.ReviewDto;
import com.example.project.entity.Member;
import com.example.project.service.MemberService;
import com.example.project.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

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
    public String submitReview(@ModelAttribute ReviewDto reviewDto) {
        // @ModelAttribute를 사용해 자동으로 DTO 매핑
        Long memberId = reviewDto.getMemberId();
        Long movieId = reviewDto.getMovieId();

        // 디버깅 로그
        System.out.println("Received Member ID: " + memberId);
        System.out.println("Received Movie ID: " + movieId);

        // 추가 로직 수행
        reviewService.saveReview(reviewDto);

        return "redirect:/movieDetail?id=" + movieId; // 상세 페이지로 리다이렉트
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/movieDetail")
    public String getMovieDetail(
            Model model,
            @AuthenticationPrincipal AuthMemberDto authMemberDto,
            @RequestParam("id") Long movieId) {
        if (authMemberDto == null) {
            throw new IllegalArgumentException("로그인된 사용자 정보가 없습니다.");
        }

        MemberDto memberDto = authMemberDto.getMemberDto();
        if (memberDto == null) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }

        System.out.println("Member ID: " + memberDto.getMid());
        System.out.println("Movie ID: " + movieId);

        model.addAttribute("currentMemberId", memberDto.getMid());
        model.addAttribute("currentMovieId", movieId);

        return "movieDetail";
    }

}