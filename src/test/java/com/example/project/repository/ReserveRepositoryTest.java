package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.project.entity.Genre;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.reserve.Auditorium;
import com.example.project.entity.reserve.Screening;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.movie.MovieRepository;
import com.example.project.repository.reserve.AuditoriumRepository;
import com.example.project.repository.reserve.ScreeningRepository;
import com.example.project.repository.reserve.TheaterRepository;
import com.example.project.service.MovieService;

@SpringBootTest
@Transactional
public class ReserveRepositoryTest {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private MovieService movieService;

}