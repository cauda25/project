package com.example.project.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.admin.service.test.UserService;
import com.example.project.entity.Member;

import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
@Log4j2
public class DormancyContoller {

    private final UserService userServie;

    @GetMapping("/dormancy")
    public void getMethodName(Long mid, Model model) {
        List<Member> last = userServie.findLastLogin(mid);

        model.addAttribute("last", last);
    }

    @PostMapping("/dormancy")
    public String postMethodName(@RequestParam("mid") Long mid) {
        log.info("휴면해제 폼 {}", mid);
        userServie.reactivateAccount(mid);

        return "redirect:/movie/main";

    }

}
