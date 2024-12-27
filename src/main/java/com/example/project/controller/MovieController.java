package com.example.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.MoviePersonDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.PersonDto;
import com.example.project.entity.Movie;
import com.example.project.entity.Person;
import com.example.project.entity.constant.MemberRole;
import com.example.project.service.GenreService;
import com.example.project.service.MemberFavoriteMovieService;
import com.example.project.service.MovieService;
import com.example.project.service.PersonService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final PersonService peopleService;
    private final MemberFavoriteMovieService memberFavoriteMovieService;

    @GetMapping("/main")
    public void getHome(@ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("home 폼 요청");
        requestDto.setMovieList("reservable");
        PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
        model.addAttribute("movies", movies);
    }

    @GetMapping("/movieList")
    public void getMovieList(@ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("영화 전체 목록 요청 {}", requestDto);
        if (requestDto.getKeyword() != null && requestDto.getKeyword() != "") {
            if (requestDto.getType().contains("m")) {
                PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
                model.addAttribute("movies", movies);
                log.info("토탈 {}", movies.getTotalPage());
            }
            if (requestDto.getType().contains("p")) {

                PageResultDTO<PersonDto, Person> people = peopleService.getList(requestDto);
                log.info("토탈 {}", people.getTotalPage());
                log.info("인물 {}", people.getDtoList());
                model.addAttribute("people", people);

            }
        } else {
            PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
            model.addAttribute("movies", movies);
        }

        List<GenreDto> genreDtos = genreService.getGenres();
        model.addAttribute("genreDtos", genreDtos);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인한 사용자인지 확인
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            // 로그인된 사용자일 때
            AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();

            // 찜한 영화 목록 가져오기
            List<MovieDto> favoriteMovies = movieService
                    .getFavoriteMoviesByMemberId(authMemberDto.getMemberDto().getMid());

            // 찜한 영화 목록이 null일 경우 빈 리스트로 설정
            if (favoriteMovies == null) {
                favoriteMovies = new ArrayList<>();
            }
            model.addAttribute("favoriteMovies", favoriteMovies);

            // 추천 영화 목록 가져오기
            List<MovieDto> recommendMovies = movieService.recommendMovies(authMemberDto.getMemberDto().getMid());
            model.addAttribute("recommendMovies", recommendMovies);

            // 가장 많이 찜한 감독과 그 감독의 영화 리스트 가져오기
            Long mostFrequentDirector = movieService
                    .findMostFrequentDirector(authMemberDto.getMemberDto().getMid());

            if (mostFrequentDirector != null) {
                // 감독이 있을 경우만 처리
                PersonDto directorDto = peopleService.read(mostFrequentDirector);
                List<MovieDto> recommendMoviesByDirector = movieService.getMovieListByPersonId(mostFrequentDirector);

                model.addAttribute("directorDto", directorDto);
                model.addAttribute("recommendMoviesByDirector", recommendMoviesByDirector);
            } else {
                // 감독이 없는 경우, 빈 데이터 추가
                model.addAttribute("directorDto", null);
                model.addAttribute("recommendMoviesByDirector", new ArrayList<>());
            }
        } else {
            // 로그인하지 않은 사용자일 때 (익명 사용자)
            model.addAttribute("favoriteMovies", new ArrayList<>());
            model.addAttribute("recommendMovies", new ArrayList<>());
            model.addAttribute("directorDto", null);
            model.addAttribute("recommendMoviesByDirector", new ArrayList<>());
        }

    }

    @GetMapping("/movieDetail")
    public void getMovieDetail(Long id, @ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("movieDetail 폼 요청 {}", id);

        MovieDto movieDto = movieService.getMovieDetail(id);
        model.addAttribute("movieDto", movieDto);

        List<PersonDto> directorList = new ArrayList<>();
        List<PersonDto> actorList = new ArrayList<>();
        for (PersonDto peopleDto : movieDto.getPersonDtos()) {
            for (MoviePersonDto moviePeopleDto : peopleDto.getMoviePersonDtos()) {
                if (moviePeopleDto.getRole() != null && moviePeopleDto.getRole().equals("Director")) {
                    directorList.add(peopleDto);
                } else if (moviePeopleDto.getRole() != null && moviePeopleDto.getRole().equals("Actor")) {
                    actorList.add(peopleDto);
                }
            }
        }
        model.addAttribute("directorList", directorList);
        model.addAttribute("actorList", actorList);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogin = authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String);

        // 로그인한 사용자인지 확인
        if (isLogin) {
            // 로그인된 사용자일 때
            AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
            List<Movie> favoriteMovies = memberFavoriteMovieService
                    .getFavoriteMoviesByMemberId(authMemberDto.getMemberDto().getMid());
            model.addAttribute("favoriteMovies", favoriteMovies);
            model.addAttribute("isExist",
                    memberFavoriteMovieService.existsByMemberIdAndMovieId(authMemberDto.getMemberDto().getMid(), id));
        } else {
            // 로그인하지 않은 사용자일 때 (익명 사용자)
            model.addAttribute("favoriteMovies", new ArrayList<>());
            model.addAttribute("isExist", false);
            model.addAttribute("isLogin", isLogin);
        }
        log.info("로그인 {}", isLogin);
    }

    @GetMapping("/personDetail")
    public void getPersonDetail(Long id, @ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("personDetail 폼 요청 {}", id);
        PersonDto peopleDto = peopleService.read(id);
        // List<MovieDto> movieDtoList = movieService.getMovieListByPersonId(id);
        model.addAttribute("peopleDto", peopleDto);
        // model.addAttribute("movieDtoList", movieDtoList);
    }
}