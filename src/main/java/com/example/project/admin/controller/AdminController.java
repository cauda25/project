package com.example.project.admin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.admin.dto.test.AdminCreateDto;
import com.example.project.admin.dto.test.AdminInquiryDto;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.service.test.UserService;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Genre;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;

import groovyjarjarpicocli.CommandLine.Parameters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    public void getCreate(Model model, @ModelAttribute("requestDto") PageRequestDTO pageRequestDTO,
            @RequestParam(value = "size", required = false) Integer newSize) {
        log.info("create 폼 요청");

        if (newSize != null) {
            pageRequestDTO.updateSize(15);
        }
        // List<MovieDetailsDTO> movieDetails = userServie.getMovieDetails();
        PageResultDTO<MovieDetailsDTO, Object[]> movieDetails = userServie.getMovieDetails(pageRequestDTO);
        List<MovieDetailsDTO> movieActers = userServie.movieActers();
        List<MovieDetailsDTO> movieDirector = userServie.movieDirector();

        model.addAttribute("movieList", movieDetails);
        model.addAttribute("size", pageRequestDTO.getSize());
        model.addAttribute("movieActers", movieActers);
        model.addAttribute("movieDirector", movieDirector);
    }

    @GetMapping("/join")
    public void getJoin(Model model, @ModelAttribute AdminCreateDto adminCreateDto) {
        log.info("join 폼 요청");

        List<GenreDto> genre = userServie.getAllGenres();

        model.addAttribute("genres", genre);

    }

    // @PostMapping("/join")
    // public String postMethodName(@Valid @ModelAttribute AdminCreateDto
    // adminCreateDto,
    // BindingResult bindingResult, @RequestParam("posterFile") MultipartFile file,
    // Model model) {
    // log.info("어드민 등록 폼 요청 {}", adminCreateDto);
    // if (bindingResult.hasErrors()) {
    // List<GenreDto> genreList = userServie.getAllGenres(); // 기존 장르 목록 가져오기
    // model.addAttribute("genres", genreList);
    // return "/admin/page/join";
    // }

    // if (bindingResult.hasErrors()) {
    // return "/admin/page/join";
    // }

    // // 1. 파일 저장 경로 설정
    // String uploadDir = "C:/upload/images/"; // 서버에 저장할 경로
    // String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

    // try {
    // // 2. 파일 저장
    // File saveFile = new File(uploadDir, fileName);
    // file.transferTo(saveFile);

    // // 3. 파일 경로 DB에 저장
    // String filePath = "/upload/images/" + fileName;

    // Movie movie = Movie.builder()
    // .title(adminCreateDto.getTitle())
    // .overview(adminCreateDto.getOverview())
    // .releaseDate(adminCreateDto.getReleaseDate())
    // .posterPath(filePath) // 저장한 파일 경로 추가
    // .build();

    // userServie.addMovieWithDetails(
    // movie,
    // adminCreateDto.getGenreIds(),
    // adminCreateDto.getActors(),
    // adminCreateDto.getDirectorName());

    // } catch (IOException e) {
    // log.error("파일 업로드 실패", e);
    // model.addAttribute("fileError", "포스터 업로드에 실패했습니다.");
    // return "/admin/page/join";
    // }

    // return "redirect:/admin/page/create?page=1&size=15";
    // }
    @PostMapping("/join")
    public String postMethodName(@Valid @ModelAttribute AdminCreateDto adminCreateDto,
            BindingResult bindingResult,
            @RequestParam(value = "posterFile", required = false) MultipartFile file,
            Model model) {
        log.info("어드민 등록 폼 요청 {}", adminCreateDto);

        // 유효성 검사 실패 시 → 기존 장르 목록 유지
        if (bindingResult.hasErrors()) {
            List<GenreDto> genreList = userServie.getAllGenres(); // 기존 장르 목록 가져오기
            model.addAttribute("genres", genreList);
            return "/admin/page/join";
        }

        String filePath = null;

        // 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            String uploadDir = "C:/upload/images/"; // 서버에 저장할 경로
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            try {
                // 저장 경로가 없으면 생성
                Files.createDirectories(Paths.get(uploadDir));

                // 파일 저장
                File saveFile = new File(uploadDir, fileName);
                file.transferTo(saveFile);

                // 저장된 파일 경로 설정
                filePath = "/upload/images/" + fileName;
            } catch (IOException e) {
                log.error("파일 업로드 실패", e);
                model.addAttribute("fileError", "포스터 업로드에 실패했습니다.");
                return "/admin/page/join";
            }
        }

        // 영화 정보 저장
        Movie movie = Movie.builder()
                .title(adminCreateDto.getTitle())
                .overview(adminCreateDto.getOverview())
                .releaseDate(adminCreateDto.getReleaseDate())
                .posterPath(filePath) // 파일이 없으면 null 값 저장
                .voteAverage(0.0)
                .voteCount(null)
                .build();

        userServie.addMovieWithDetails(
                movie,
                adminCreateDto.getGenreIds(),
                adminCreateDto.getActors(),
                adminCreateDto.getDirectorName());

        return "redirect:/admin/page/create?page=1&size=15";
    }

    @GetMapping({ "/movie", "/movieAdd" })
    public void getMovie(String state, String theaterName, Model model, TheaterDto aDto) {
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
        return "redirect:/admin/page/movie";

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
