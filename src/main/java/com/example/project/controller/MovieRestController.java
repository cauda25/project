package com.example.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.service.MemberFavoriteMovieService;
import com.example.project.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
@RestController
public class MovieRestController {

    private final MovieService movieService;
    private final MemberFavoriteMovieService memberFavoriteMoviesService;

    // @GetMapping("/movieList")
    // public ResponseEntity<PageResultDTO<MovieDto, Movie>> getList(
    // @ModelAttribute("requestDto") @RequestBody PageRequestDTO requestDto) {
    // log.info("rest 영화 전체 목록 요청 {}", requestDto);
    // PageResultDTO<MovieDto, Movie> result = movieService.getList(requestDto);
    // return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    @GetMapping("/movieDetail/{id}")
    public ResponseEntity<MovieDto> getMovieDetail(@PathVariable Long id,
            @ModelAttribute("requestDto") @RequestBody PageRequestDTO requestDto) {
        log.info("rest 영화 전체 목록 요청 {}", requestDto);
        MovieDto movieDto = movieService.getMovieDetail(id);
        return new ResponseEntity<>(movieDto, HttpStatus.OK);
    }

    @GetMapping("/movieDetail/director/{id}")
    public ResponseEntity<List<MovieDto>> getMoviesByDirector(@PathVariable Long id,
            @ModelAttribute("requestDto") @RequestBody PageRequestDTO requestDto) {
        log.info("rest 영화 전체 목록 요청 {}", requestDto);
        List<MovieDto> movieDtos = movieService.getMovieListByPersonId(id);
        return new ResponseEntity<>(movieDtos, HttpStatus.OK);
    }

    @GetMapping("/movieDetail/genres/{id}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenres(@PathVariable Long id,
            @ModelAttribute("requestDto") @RequestBody PageRequestDTO requestDto) {
        log.info("rest 영화 전체 목록 요청 {}", requestDto);
        List<MovieDto> movieDtos = movieService.similarMovies(id);
        return new ResponseEntity<>(movieDtos, HttpStatus.OK);
    }

    @GetMapping("/person/detail/{id}")
    public ResponseEntity<List<MovieDto>> getPersonDetail(@PathVariable Long id,
            @ModelAttribute("requestDto") @RequestBody PageRequestDTO requestDto) {
        log.info("rest 영화 전체 목록 요청 {}", id);
        List<MovieDto> movieDtoList = movieService.getMovieListByPersonId(id);
        log.info("rest 영화 전체 목록 요청 {}", movieDtoList);

        return new ResponseEntity<>(movieDtoList, HttpStatus.OK);
    }

    @PostMapping("/movieDetail/{id}")
    public ResponseEntity<String> addMovieToFavorites(@PathVariable Long id) {
        log.info("rest 영화 찜하기 요청 {}", id);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        if (!memberFavoriteMoviesService.existsByMemberIdAndMovieId(authMemberDto.getMemberDto().getMid(), id)) {
            try {
                memberFavoriteMoviesService.addFavoriteMovie(authMemberDto.getMemberDto().getMid(), id);
                return ResponseEntity.ok("영화가 찜 목록에 추가되었습니다.");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            memberFavoriteMoviesService.deleteFavoriteMovie(authMemberDto.getMemberDto().getMid(), id);
            return ResponseEntity.ok("영화가 찜 목록에서 제거되었습니다.");
        }
    }
}
