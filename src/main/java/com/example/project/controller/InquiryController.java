package com.example.project.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.dto.MemberDto;
import com.example.project.entity.Inquiry;
import com.example.project.entity.InquiryStatus;
import com.example.project.entity.Member;
import com.example.project.service.InquiryService;
import com.example.project.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/center/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final MemberService memberService;

    @Autowired
    public InquiryController(InquiryService inquiryService, MemberService memberService) {
        this.inquiryService = inquiryService;
        this.memberService = memberService;
    }

    // 문의 저장
    @PostMapping("/save")
    public String saveInquiry(@ModelAttribute Inquiry inquiry, @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String memberId = userDetails.getUsername();
        MemberDto memberDto = memberService.getMemberById(memberId);

        if (memberDto == null) {
            return "redirect:/login"; // 회원 정보가 없으면 로그인 페이지로 리다이렉트
        }

        // inquiry에 사용자 정보 설정
        inquiry.setMember(Member.builder().mid(memberDto.getMid()).build());
        inquiry.setUsername(memberId);
        inquiry.setName(memberDto.getName());
        inquiry.setEmail(memberDto.getEmail());
        inquiry.setPhone(memberDto.getPhone());

        // 서비스에서 저장 처리
        inquiryService.saveInquiry(inquiry);

        return "redirect:/center/my-inquiries"; // 저장 후 사용자 문의 내역 페이지로 이동
    }

    @GetMapping("/center/my-inquiries")
    public String viewInquiries(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session) {
        // Pageable을 생성하여 전달
        Pageable pageable = PageRequest.of(page - 1, 10); // 0-based index로 설정

        // 페이징된 문의 내역을 가져옴
        Page<Inquiry> inquiriesPage = inquiryService.getAllInquiries(pageable);

        // inquiriesPage에서 content를 꺼내어 inquiries에 전달
        model.addAttribute("inquiries", inquiriesPage.getContent());

        // 페이지 정보 전달
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inquiriesPage.getTotalPages());

        // 새로 작성된 글을 Flash Attribute에서 가져와서 모델에 추가
        Inquiry tempInquiry = (Inquiry) session.getAttribute("tempInquiry");
        if (tempInquiry != null) {
            model.addAttribute("tempInquiry", tempInquiry);
            session.removeAttribute("tempInquiry"); // 한 번 조회 후 삭제
        }

        return "center/my-inquiries"; // 나의 문의 내역 페이지로 이동
    }

    // 관리자 - 문의 목록 페이지
    @PostMapping("/updateInquiryStatus")
    public String updateInquiryStatus(@RequestParam Long inquiryId, @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        try {
            inquiryService.updateInquiryStatus(inquiryId, status);
            redirectAttributes.addFlashAttribute("message", "상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "상태 변경 중 오류가 발생했습니다.");
        }
        return "redirect:/center/inquiry/list";
    }

    @PostMapping("/center/inquiry/answer/{id}")
    public String answerInquiry(@PathVariable("id") Long inquiryId, @RequestParam("answer") String answer) {
        // 상담 내용을 찾아오기
        Inquiry inquiry = inquiryService.findInquiryById(inquiryId);

        // 답변을 작성한 경우
        if (answer != null && !answer.isEmpty()) {
            inquiry.setAnswer(answer);
            inquiry.setStatus(InquiryStatus.ANSWERED); // 상태를 ANSWERED로 변경
            inquiryService.saveInquiry(inquiry);

            System.out.println("문의 상태 변경됨: " + inquiry.getStatus()); // 로그 출력
        }

        // 답변 후 해당 상담의 상세 페이지로 리다이렉트
        return "redirect:/center/inquiry/answered/" + inquiryId;
    }

    // 관리자 - 답변 상태 변경
    public String updateInquiryStatusAndAnswer(@RequestParam Long inquiryId, @RequestParam String answer,
            @RequestParam String status) {
        Inquiry inquiry = inquiryService.findInquiryById(inquiryId);

        // 답변 내용 설정
        inquiry.setAnswer(answer);

        // 상담 상태 설정
        if ("ANSWERED".equals(status)) {
            inquiry.setStatus(InquiryStatus.ANSWERED); // Status는 Enum으로 "ANSWERED", "PENDING", "CLOSED" 등이 있을 수 있음
        } else if ("PENDING".equals(status)) {
            inquiry.setStatus(InquiryStatus.PENDING);
        } else if ("CLOSED".equals(status)) {
            inquiry.setStatus(InquiryStatus.CLOSED);
        }

        inquiryService.save(inquiry);
        return "redirect:/center/inquiry/detail/" + inquiryId;
    }

    // 개별 문의 조회 (API)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getInquiry(@PathVariable String id) {
        try {
            Long inquiryId = parseInquiryId(id); // Try to parse the ID as Long
            Map<String, String> response = inquiryService.getInquiryDetails(inquiryId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Invalid inquiry ID"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // 유틸리티 메소드: ID가 숫자 문자열인지 확인하고 Long으로 변환
    private Long parseInquiryId(String id) throws NumberFormatException {
        try {
            return Long.valueOf(id); // Try to parse the string as a Long
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid inquiry ID format");
        }
    }

    @GetMapping("/center/inquiries")
    public String showInquiries(Model model) {
        Member member = inquiryService.getCurrentMember();

        Inquiry newInquiry = new Inquiry();
        newInquiry.setName(member.getName());
        newInquiry.setEmail(member.getEmail());
        newInquiry.setPhone(member.getPhone());

        model.addAttribute("inquiry", newInquiry);
        return "center/email/form";
    }

    @GetMapping("/counseling")
    public String showCounselingPage(@RequestParam(defaultValue = "1") int page, Model model) {
        // Pageable을 생성하여 전달
        Pageable pageable = PageRequest.of(page - 1, 10); // 0-based index로 설정

        // 페이징된 문의 내역을 가져옴
        Page<Inquiry> inquiriesPage = inquiryService.getAllInquiries(pageable);

        inquiriesPage.getContent()
                .forEach(inquiry -> System.out.println("문의 ID: " + inquiry.getId() + ", 상태: " + inquiry.getStatus()));

        // inquiriesPage에서 content를 꺼내어 inquiries에 전달
        model.addAttribute("inquiries", inquiriesPage.getContent());

        // 페이지 정보 전달
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inquiriesPage.getTotalPages());

        return "center/counseling"; // 상담 목록 페이지로 반환
    }

    // 로그인한 사용자의 문의 내역 조회 (수정 완료)
    @GetMapping("/my-inquiries")
    public String getUserInquiries(@RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 된 경우 로그인 페이지로 이동
        }

        String username = userDetails.getUsername(); // 현재 로그인한 사용자의 ID 가져오기

        // 사용자의 문의 내역 조회
        Page<Inquiry> inquiries = inquiryService.getInquiriesByUsername(username, page);

        model.addAttribute("inquiries", inquiries.getContent()); // 문의 목록 전달
        model.addAttribute("currentPage", page); // 현재 페이지
        model.addAttribute("totalPages", inquiries.getTotalPages()); // 총 페이지 수
        return "redirect:/center/my-inquiries"; // 문의 목록 뷰
    }

    @PostMapping("/delete-selected")
    public String deleteSelectedInquiries(@RequestParam("inquiryIds") String inquiryIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 다시 시도해주세요.");
            return "redirect:/login";
        }

        String memberId = userDetails.getUsername();
        MemberDto memberDto = memberService.getMemberById(memberId);

        if (memberDto == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
            return "redirect:/login";
        }

        try {
            List<Long> idsToDelete = Arrays.stream(inquiryIds.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // 삭제 처리
            inquiryService.deleteInquiriesByIds(idsToDelete);

            redirectAttributes.addFlashAttribute("successMessage", "문의 내역이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/center/counseling"; // 리다이렉트
    }

    // 문의 상세 조회 (답변 여부와 관계없이 같은 URL 사용)
    @GetMapping("/center/inquiry/detail/{id}")
    public String viewInquiry(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        return "center/detail"; // 하나의 화면 사용
    }

    @GetMapping("/detail/{id}")
    public String getInquiryDetail(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryService.findInquiryById(id);
        if (inquiry == null) {
            return "redirect:/center/inquiry"; // 문의 내역이 없으면 목록으로 이동
        }
        model.addAttribute("inquiry", inquiry);
        return "center/detail"; // detail.html로 이동
    }

    @PostMapping("/center/email")
    public String submitInquiry(@ModelAttribute Inquiry inquiry, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 된 경우 로그인 페이지로 이동
        }

        String username = userDetails.getUsername();
        inquiry.setUsername(username);

        // 글 저장
        inquiryService.saveInquiry(inquiry);

        return "redirect:/center/my-inquiries";
    }

}
