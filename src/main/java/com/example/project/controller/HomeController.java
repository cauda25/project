package com.example.project.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getIndex() {
        log.info("home 폼 요청");
        return "redirect:/movie/main";
    }

    @GetMapping("/access-denied")
    public String getAcc() {
        return "/except/denied";
    }

    // @GetMapping("/error")
    // public String get404() {
    // return "/except/url404";
    // }
}
