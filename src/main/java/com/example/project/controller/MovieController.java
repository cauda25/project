package com.example.project.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping("/main")
    public void getHome() {
        log.info("home 폼 요청");

    }

    @GetMapping("/reservation")
    public void getReservation() {
        log.info("home 폼 요청");

    }

    @GetMapping("/center")
    public void getCenter() {
        log.info("home 폼 요청");

    }

    @GetMapping("/movieList")
    public void getMovieList(Long genre, Long page, String movieList, String type, String keyword, Model model) {
        log.info("movieList 폼 요청 {}", page);
        model.addAttribute("movieList", movieList);
        model.addAttribute("genre", genre);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
    }

    @GetMapping("/movieDetail")
    public void getMovieDetail(Long id, String movieList, Long genre, String type, String keyword, Long page,
            Model model) {
        log.info("movieDetail 폼 요청 {}", id);
        model.addAttribute("id", id);
        model.addAttribute("movieList", movieList);
        model.addAttribute("genre", genre);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
    }

}
