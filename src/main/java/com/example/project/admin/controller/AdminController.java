package com.example.project.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.admin.dto.test.AdminCreateDto;
import com.example.project.admin.dto.test.AdminInquiryDto;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.service.test.UserService;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;

import groovyjarjarpicocli.CommandLine.Parameters;
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
    public void getUserTest(MemberDto memberDto, Long mid, Model model) {
        log.info("home 폼 요청");
        List<Member> list = userServie.allList(memberDto);
        userServie.inactiveAccounts();
        // userServie.reactivateAccount(uno);
        model.addAttribute("list", list);

    }

    @PostMapping("/user")
    public String PostUser(Long mid) {
        log.info("reactive 폼 요청 {}", mid);

        userServie.reactivateAccount(mid);
        return "redirect:/admin/page/user";

    }

    @GetMapping("/create")
    public void getCreate(Model model) {
        log.info("create 폼 요청");
        List<MovieDetailsDTO> movieDetails = userServie.getMovieDetails();
        List<MovieDetailsDTO> movieActers = userServie.movieActers();
        List<MovieDetailsDTO> movieDirector = userServie.movieDirector();

        model.addAttribute("movieList", movieDetails);
        model.addAttribute("movieActers", movieActers);
        model.addAttribute("movieDirector", movieDirector);
    }

    @GetMapping("/join")
    public void getJoin(Model model) {
        log.info("join 폼 요청");
        List<GenreDto> genre = userServie.getAllGenres();

        model.addAttribute("genres", genre);
    }

    @PostMapping("/join")
    public String postMethodName(@ModelAttribute AdminCreateDto adminCreateDto) {
        log.info("어드민 등록 폼 요청 {}", adminCreateDto);
        Movie movie = Movie.builder()
                .title(adminCreateDto.getTitle())
                .overview(adminCreateDto.getOverview())
                .releaseDate(adminCreateDto.getReleaseDate())
                .build();

        // 서비스 호출
        userServie.addMovieWithDetails(
                movie,
                adminCreateDto.getGenreIds(),
                adminCreateDto.getActors(),
                adminCreateDto.getDirectorName());

        return "redirect:/admin/page/create";
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

    @GetMapping("/answerList")
    public void getAnswerList(AdminInquiryDto adminInquiryDto, Model model) {
        log.info("home 폼 요청");
        List<Inquiry> inquiList = userServie.inquityList(adminInquiryDto);

        model.addAttribute("inquiry", inquiList);
    }

    @GetMapping("/answer")
    public void getAnswer(@RequestParam Long id, Inquiry inquiry, Model model) {
        log.info("Answer 요청, ID: {}", id);
        Inquiry inid = userServie.getInquity(id);

        model.addAttribute("inid", inid);

    }

    @PostMapping("/answer")
    public String postAnswer(Inquiry inquiry, String answer) {
        log.info("Received email: {}", inquiry.getEmail());
        userServie.insertInquity(inquiry, answer);

        return "redirect:/admin/page/answerList";
    }

}
