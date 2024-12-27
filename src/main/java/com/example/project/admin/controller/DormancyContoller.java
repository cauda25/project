package com.example.project.admin.controller;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.admin.service.test.UserServie;

import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
@Log4j2
public class DormancyContoller {

    private final UserServie userServie;

    @GetMapping("/dormancy")
    public void getMethodName() {

    }

    @PostMapping("/dormancy")
    public String postMethodName(@RequestParam("uno") Long uno) {
        log.info("휴면해제 폼 {}", uno);
        userServie.reactivateAccount(uno);

        return "redirect:/admin/page/index";

    }

}
