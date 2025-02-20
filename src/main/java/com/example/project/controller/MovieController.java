package com.example.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.example.project.dto.ReviewDto;
import com.example.project.entity.Movie;
import com.example.project.entity.Person;
import com.example.project.service.GenreService;
import com.example.project.service.MemberFavoriteMovieService;
import com.example.project.service.MovieService;
import com.example.project.service.PersonService;
import com.example.project.service.ReviewService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final GenreService genreService;
    private final PersonService personService;
    private final MemberFavoriteMovieService memberFavoriteMovieService;

    @GetMapping("/main")
    public void getHome(@ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("home 폼 요청");
        requestDto.setMovieList("reservable");
        PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
        model.addAttribute("movies", movies);
    }

    @GetMapping("/list")
    public String getMovieList(@ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("영화 전체 목록 요청 {}", requestDto);
        if ((requestDto.getKeyword() != null && !requestDto.getKeyword().isEmpty())
                || (requestDto.getType() != null && !requestDto.getType().isEmpty())) {
            if (requestDto.getType().contains("m")) {
                PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
                model.addAttribute("movies", movies);
            }
            if (requestDto.getType().contains("p")) {
                PageResultDTO<PersonDto, Person> people = personService.getList(requestDto);
                model.addAttribute("people", people);
            }
        } else {
            // 'keyword'가 없고 'type'이 없으면 기본적으로 영화 목록만 추가
            PageResultDTO<MovieDto, Movie> movies = movieService.getList(requestDto);
            model.addAttribute("movies", movies);
        }

        List<GenreDto> genreDtos = genreService.getGenres();
        model.addAttribute("genreDtos", genreDtos);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 추천 페이지일 때
        if (requestDto.getMovieList().equals("my")) {
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
                    PersonDto directorDto = personService.read(mostFrequentDirector);
                    List<MovieDto> recommendMoviesByDirector = movieService
                            .getMovieListByDirectorId(mostFrequentDirector);

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
                return "redirect:/member/login";
            }

        }
        return null;

    }

    @GetMapping("/detail")
    public void getMovieDetail(Long id, @ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("movieDetail 폼 요청 {}", id);

        // movieService.findById(id) 대신 movieService.getMovieDetail(id) 사용
        MovieDto movieDto = movieService.getMovieDetail(id);
        model.addAttribute("movieDto", movieDto);

        // 영화의 인물 정보 중 감독/출연 분류
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
        if (directorList.isEmpty()) {
            directorList.add(PersonDto.builder().id(0L).name("정보 없음").build());
        }
        model.addAttribute("directorList", directorList);
        model.addAttribute("actorList", actorList);

        // 로그인 여부 및 사용자 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogin = authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isLogin", isLogin);

        if (isLogin) {
            AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
            Long memberId = authMemberDto.getMemberDto().getMid();
            model.addAttribute("memberId", memberId);

            List<Movie> favoriteMovies = memberFavoriteMovieService.getFavoriteMoviesByMemberId(memberId);
            model.addAttribute("favoriteMovies", favoriteMovies);
            model.addAttribute("isExist", memberFavoriteMovieService.existsByMemberIdAndMovieId(memberId, id));
        } else {
            model.addAttribute("memberId", "");
            model.addAttribute("favoriteMovies", new ArrayList<>());
            model.addAttribute("isExist", false);
        }
        log.info("로그인 {}", isLogin);

        // ★ 여기서 리뷰 목록을 추가합니다.
        List<ReviewDto> reviews = reviewService.findReviewsByMovieId(id);
        model.addAttribute("reviews", reviews);
    }

    @GetMapping("/person/detail")
    public void getPersonDetail(Long id, @ModelAttribute("requestDto") PageRequestDTO requestDto,
            Model model) {
        log.info("personDetail 폼 요청 {}", id);
        PersonDto peopleDto = personService.read(id);
        model.addAttribute("peopleDto", peopleDto);
    }

}