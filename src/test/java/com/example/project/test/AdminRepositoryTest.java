package com.example.project.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import com.example.project.admin.Entity.Admin;
import com.example.project.admin.Entity.MovieState;
import com.example.project.admin.Entity.constant.AdminRole;
import com.example.project.admin.repository.AdminMovieRepository;
import com.example.project.admin.repository.AdminRepository;
import com.example.project.admin.repository.MovieAddRepository;
import com.example.project.admin.repository.MovieStateRepository;
import com.example.project.repository.test.UserRepository;
import com.example.project.entity.Genre;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.reserve.Theater;
import com.example.project.entity.test.UserEntity;
import com.example.project.repository.movie.GenreRepository;
import com.example.project.repository.movie.MovieGenreRepository;
import com.example.project.repository.movie.MovieRepository;

import com.example.project.service.MovieService;
import com.querydsl.core.types.Predicate;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;
    @Autowired
    private AdminMovieRepository adminMovieRepository;
    @Autowired
    private MovieAddRepository movieAddRepository;
    @Autowired
    private MovieStateRepository movieStateRepository;

    @Test
    public void insertAdmin() {
        Admin admin = Admin.builder().userId("user1").password(passwordEncoder.encode("1111"))
                .adminRole(AdminRole.ADMIN).build();
        adminRepository.save(admin);
    }

    @Test
    public void userInsert() {
        LongStream.rangeClosed(1, 50).forEach(i -> {
            int re = ((int) (Math.random() * 2));
            int year = ((int) (Math.random() * 50) + 1980);
            int month = ((int) (Math.random() * 12) + 1);
            int day = ((int) (Math.random() * 31) + 1);
            UserEntity userEntity = UserEntity.builder()
                    .userId("userId" + i)
                    .password(passwordEncoder.encode("1111"))
                    .name("name" + i)
                    .email("usermeil" + i + "@gmail.com")
                    .gender(re)
                    .brith(year + "/" + month + "/" + day)
                    .telNo("010-0000-0000")
                    .adminRole(AdminRole.USER)
                    .build();

            userRepository.save(userEntity);
        });
    }

    @Test
    public void userList() {
        userRepository.findAll().forEach(u -> {
            System.out.println(u.getUserId());
            System.out.println(u.getName());
            System.out.println(u.getEmail());
            System.out.println(u.getGender());
        });
        ;
    }

    // @Transactional
    // @Test
    // public void getGenre() {
    // List<Object[]> results = movieRepository.getMovieDetailsWithGenres(1159311L);

    // for (Object[] objects : results) {

    // // System.out.println(Arrays.toString(objects));

    // Movie movie = (Movie) objects[0];
    // Genre genre = (Genre) objects[1];
    // System.out.println(movie.getTitle());
    // System.out.println(movie.getReleaseDate());
    // System.out.println(genre.getName());

    // }

    // 데이터를 정리할 Map
    // Map<String, Map<String, List<String>>> movieDetails = new HashMap<>();
    // for (Object[] result : results) {
    // String title = (String) result[0]; // 영화 제목
    // String releaseDate = (String) result[1]; // 문자열로 된 개봉일
    // String genre = (String) result[2]; // 장르 이름

    // // 데이터를 Map에 추가
    // movieDetails
    // .computeIfAbsent(title, k -> new HashMap<>())
    // .computeIfAbsent(releaseDate, k -> new ArrayList<>())
    // .add(genre);
    // }

    // // 정리된 데이터를 출력
    // movieDetails.forEach((title, details) -> {
    // details.forEach((releaseDate, genres) -> {
    // System.out.println("Movie: " + title + ", Release Date: " + releaseDate +
    // ", Genres: " + String.join(", ", genres));
    // });
    // });

    // 영화 제목 , 장르 ,개봉일
    @Transactional
    @Test
    public void getGenres() {

        movieRepository.findAll().forEach(movie -> {
            System.out.println(movie);

            movie.getMoviePeople().forEach(people -> System.out.println(people.getPerson().getName()));

            movie.getMovieGenres().forEach(genre -> System.out.println(genre.getGenre().getName()));
        });
        ;

        // List<Object[]> result = adminMovieRepository.getMovieDetails();

        // for (Object[] objects : result) {

        // System.out.println(Arrays.toString(objects));

        // // String title = (String) objects[0];
        // // String releaseDate = (String) objects[1];
        // // String genres = (String) objects[2];
        // // System.out.println(title);
        // // System.out.println(releaseDate);
        // // System.out.println(genres);

        // }
    }

    // 영화 배우
    @Transactional
    @Test
    public void getActers() {
        List<Object[]> result = adminMovieRepository.movieActers();

        for (Object[] objects : result) {

            // System.out.println(Arrays.toString(objects));

            String name = (String) objects[1];
            System.out.println(name);

        }
    }

    @Commit
    @Transactional
    @Test
    public void addMovieWithGenres() {

        Movie movie = Movie.builder()
                .title("test test test test")
                .overview("왜 안돼는가??")
                .releaseDate("2024-12-20")
                .build();
        movieRepository.save(movie);

        System.out.println(movie);

        // Genre genre = genreRepository.findById(28L).get(); // 장르 하나 추가
        List<Long> genreIds = List.of(28L, 80L, 16L); // 장르 여러개 추가
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("선택하신 장르를 찾을 수 없습니다. : " + genreId)); // 선택한 장르가 없을때
            System.out.println(genre);

            MovieGenre movieGenre = MovieGenre.builder()
                    .movie(movie)
                    .genre(genre)
                    .build();

            movieGenreRepository.save(movieGenre);
            System.out.println(movieGenre);
        }
    }

    @Transactional
    @Test
    public void addTest() {
        List<Object[]> list = movieAddRepository.findByAddList();
        for (Object[] objects : list) {

            // System.out.println(Arrays.toString(objects));

            String state = (String) objects[0];
            System.out.println(state);

        }
    }

    // @Transactional
    // @Test
    // public void selectTest() {
    // List<Object[]> state = movieAddRepository.findByState("서울특별시");
    // for (Object[] objects : state) {
    // System.out.println(Arrays.toString(objects));
    // }

    // }

    @Transactional
    @Test
    public void selectTest2() {
        List<Object[]> state = movieAddRepository.stateAndName("", "가");

        // List<Object[]> state = movieAddRepository.stateSearch("강");
        // List<Object[]> state = movieAddRepository.nameSearch("충주교현");
        for (Object[] objects : state) {
            // System.out.println(Arrays.toString(objects));
            Theater t = (Theater) objects[0];
            System.out.println(t);
        }

    }

    @Commit
    @Transactional
    @Test
    public void insetStateTest() {
        MovieState state = movieStateRepository.findById(9L).get();

        Theater movieAdd = Theater.builder()
                .theaterName("test test test")
                .theaterAdd("서울특별시")
                .theaterState("서울특별시")
                .movieState(state)
                .build();

        movieAddRepository.save(movieAdd);
        System.out.println(movieAdd);
    }

    @Test
    public void deleteTest() {
        movieAddRepository.deleteById(3051L);

    }

}
