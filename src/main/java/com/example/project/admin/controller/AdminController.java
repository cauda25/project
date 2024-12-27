package com.example.project.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.service.test.UserService;
import com.example.project.dto.MemberDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.test.UserEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/admin/page")
public class AdminController {

    private final UserService userServie;

    @GetMapping("/index")
    public void getHome() {
        log.info("home 폼 요청");

    }

    @GetMapping("/user")
    public void getUser(MemberDto memberDto, Model model) {
        log.info("home 폼 요청");
        List<Member> list = userServie.allList(memberDto);
        model.addAttribute("list", list);

    }

    @GetMapping("/create")
    public void getCreate(Model model) {
        log.info("create 폼 요청");
        // 서비스 호출
        List<MovieDetailsDTO> movieDetails = userServie.getMovieDetails();
        List<MovieDetailsDTO> movieActers = userServie.movieActers();

        // 모델에 데이터 추가
        model.addAttribute("movieList", movieDetails);
        model.addAttribute("movieActers", movieActers);
    }

    @GetMapping("/join")
    public void getJoin() {
        log.info("join 폼 요청");

    }

    @GetMapping({ "/movie", "/movieAdd" })
    public void getMovie(String state, String theaterName, Model model) {
        log.info("movie 폼 요청 {} {}", state, theaterName);
        List<TheaterDto> add = userServie.selectList(state, theaterName);
        model.addAttribute("add", add);
        model.addAttribute("states", userServie.getAllStates());
        model.addAttribute("state", state);
        model.addAttribute("name", theaterName);
        log.info("movie 폼 요청 {}", model);

    }

    @PostMapping("/movieAdd")
    public String postMovieAdd(@Valid TheaterDto aDto, BindingResult result, Model model, RedirectAttributes rttr,
            String theaterState) {
        log.info("영화관등록 폼 요청 {} ", aDto);
        if (result.hasErrors()) {
            return "redirect:/admin/page/movieAdd";
        }

        Long add = userServie.addMovie(aDto);

        model.addAttribute("add", add);
        rttr.addAttribute("state", theaterState);
        return "redirect:/admin/page/movie?";

    }

    // @PostMapping("/remove")
    // public String postRemove(Long tno) {
    // log.info("영화관삭제 폼 요청 {} ", tno);
    // userServie.delete(tno);

    // return "redirect:/admin/page/movie";
    // }

}
