package com.example.project.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.reserve.ReserveDto;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.repository.MemberRepository;
import com.example.project.service.MemberFavoriteMovieService;
import com.example.project.service.MemberService;
import com.example.project.service.MovieService;
import com.example.project.service.reservation.ReserveService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final ReserveService reserveService;
    private final MemberRepository memberRepository;
    private final MovieService movieService;
    private final MemberFavoriteMovieService memberFavoriteMovieService;

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
    public String getMypage(Model model, Principal principal, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        String memberId = principal.getName();

        try {
            // 회원 정보 가져오기
            MemberDto memberDto = memberService.getMemberById(memberId);
            model.addAttribute("member", memberDto);

            // 찜 목록 가져오기
            List<MovieDto> favorites = movieService.getFavoriteMoviesByMemberId(memberDto.getMid());
            model.addAttribute("favorites", favorites);

            return "member/mypage"; // 정상적으로 마이페이지 반환

        } catch (RuntimeException e) {
            // 예외 발생 시 강제 로그아웃 (회원 정보 없음)
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();

            redirectAttributes.addFlashAttribute("error", "회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
            return "redirect:/member/login";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminpage")
    public void getAdminPage() {
        log.info("adminpage 폼 요청");
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
        Long mid = authMemberDto.getMemberId(); // 인증된 사용자 ID 가져오기
        log.info("사용자" + mid);
        List<ReserveDto> reservations = reserveService.getMemberReservations(mid);
        model.addAttribute("reservations", reservations);
        return "member/reservation";
    }

    @GetMapping("/paymentDetails")
    public void getPaymentDetails(@AuthenticationPrincipal AuthMemberDto authMemberDto, Model model) {
        Long memberId = authMemberDto.getMemberId(); // 인증된 사용자 ID 가져오기
        // List<ReservationDto> reservation =
        // reservationService.getMemberReservations(memberId);
        // model.addAttribute("reservation", reservation);
    }

    @GetMapping("/confirm-delete")
    public String confirmDeletePage(Model model, @AuthenticationPrincipal AuthMemberDto authMember) {
        if (authMember == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리디렉트
        }

        model.addAttribute("memberId", authMember.getMemberId()); // 사용자 ID를 Thymeleaf에서 사용 가능하도록 전달
        return "member/confirm-delete"; // 회원 탈퇴 확인 페이지
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        try {
            log.info("회원 탈퇴 요청: " + memberId);

            // 존재하는 회원인지 확인
            Optional<Member> member = memberRepository.findById(memberId);
            if (!member.isPresent()) {
                log.warn("회원 없음: " + memberId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("회원 정보를 찾을 수 없습니다.");
            }

            // 회원 삭제
            memberService.deleteMember(memberId);
            return ResponseEntity.ok("회원 탈퇴 성공");
        } catch (Exception e) {
            log.error("회원 탈퇴 오류:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 탈퇴 중 오류 발생");
        }
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