package com.example.project.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.entity.Movie;
import com.example.project.repository.movie.MovieRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {
    private final MovieRepository movieRepository;

    @Value("${com.example.movie.upload.path}")
    private String uploadPath;

    @GetMapping("/upload")
    public void getMethodName() {
        log.info("업로드 폼 요청");

    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("movieId") Long movieId,
            RedirectAttributes redirectAttributes) {
        try {
            // 파일 저장 경로 설정
            String uploadDir = "C:/upload/images/"; // 로컬 경로 or 서버 경로
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, fileName);
            file.transferTo(saveFile); // 파일 저장

            // DB에 저장할 파일 경로 생성
            String filePath = "/upload/images/" + fileName;

            // DB에 업데이트
            Optional<Movie> movieOptional = movieRepository.findById(movieId);
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                movie.setPosterPath(filePath);
                movieRepository.save(movie);
            }

            redirectAttributes.addFlashAttribute("message", "파일 업로드 성공!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "파일 업로드 실패!");
            e.printStackTrace();
        }
        return "redirect:/admin/page/create"; // 업로드 후 이동할 페이지
    }
}