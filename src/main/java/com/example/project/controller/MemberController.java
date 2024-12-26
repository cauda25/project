package com.example.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.ReservationDto;
import com.example.project.service.FavoriteService;
import com.example.project.service.MemberService;
import com.example.project.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor // Lombok 자동 생성자 주입
public class MemberController {

    private final MemberService memberService;
    private final FavoriteService favoriteService;
    private final ReservationService reservationService;

    @GetMapping("/login")
    public void loginRedirect() {
        log.info("로그인 폼 요청");

    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/register"; // Thymeleaf 템플릿 경로
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("memberDto") MemberDto memberDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.info("회원가입 {}", memberDto);

        // 입력값 유효성 검사
        if (bindingResult.hasErrors()) {
            return "member/register";
        }

        // 아이디 중복 검사
        if (memberService.isMemberIdDuplicate(memberDto.getMemberId())) {
            model.addAttribute("idError", "이미 사용 중인 아이디입니다.");
            return "member/register";
        }

        // 이메일 중복 검사
        if (memberService.isEmailDuplicate(memberDto.getEmail())) {
            model.addAttribute("emailError", "이미 사용 중인 이메일입니다.");
            return "member/register";
        }

        // 회원가입 처리
        memberService.registerMember(memberDto);
        redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다.");
        return "redirect:/member/login";
    }

    @GetMapping("/mypage")
    public String getMypage(Model model, Principal principal) {
        // 현재 로그인한 사용자 ID 가져오기
        String memberId = principal.getName();

        // 회원정보 가져오기
        MemberDto memberDto = memberService.getMemberById(memberId);
        model.addAttribute("member", memberDto);

        // 찜 목록 가져오기
        List<MovieDto> favorites = favoriteService.getFavorites(memberId);
        model.addAttribute("favorites", favorites);

        return "member/mypage"; // mypage.html 반환
    }
    // @GetMapping("/logout")
    // public String logout(HttpSession session) {
    // // 세션 무효화 (로그아웃 처리)
    // session.invalidate();
    // return "member/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    // }

    // 비밀번호 확인 페이지 렌더링
    @GetMapping("/verify-password")
    public String verifyPasswordForm(Principal principal, Model model) {
        String memberId = principal.getName();

        if (memberId == null) {
            return "redirect:/member/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        model.addAttribute("memberId", memberId);
        return "member/verify-password"; // 비밀번호 확인 페이지
    }

    // 비밀번호 검증
    @PostMapping("/verify-password")
    public String verifyPassword(Principal principal, String password, Model model) {
        String memberId = principal.getName();

        if (memberId == null) {
            return "redirect:/member/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        boolean isValid = memberService.verifyPassword(memberId, password);

        if (!isValid) {
            model.addAttribute("error", "비밀번호가 잘못되었습니다.");
            return "member/verify-password";
        }

        return "redirect:/member/edit"; // 비밀번호가 올바르면 수정 페이지로
    }

    @GetMapping("/edit")
    public String editMemberForm(Principal principal, Model model) {
        String memberId = principal.getName();

        if (memberId == null) {
            return "redirect:/member/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        MemberDto memberDto = memberService.getMemberById(memberId);
        model.addAttribute("member", memberDto);
        return "member/edit"; // 수정 페이지
    }

    @PostMapping("/edit")
    public String editMember(@ModelAttribute("member") MemberDto memberDto, Principal principal, Model model) {
        // 세션에서 현재 로그인된 사용자 확인
        String memberId = principal.getName();
        if (memberId == null) {
            return "redirect:/member/login"; // 로그인하지 않은 경우
        }

        // DTO에 세션에서 가져온 memberId 설정
        memberDto.setMemberId(memberId);

        // 서비스 호출하여 회원정보 업데이트
        try {
            memberService.updateMember(memberDto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "member/edit";
        }

        return "redirect:/member/mypage"; // 성공 시 마이페이지로 리다이렉트
    }

    @GetMapping("/reservation")
    public String getReservation(@AuthenticationPrincipal AuthMemberDto authMemberDto, Model model) {
        Long memberId = authMemberDto.getMemberId(); // 인증된 사용자 ID 가져오기
        List<ReservationDto> reservation = reservationService.getMemberReservations(memberId);
        model.addAttribute("reservation", reservation);
        return "member/reservation"; // 템플릿 경로 수정 (member 디렉토리로 변경)
    }

    // 개발자용 - Authentication 확인용
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;
    }
}