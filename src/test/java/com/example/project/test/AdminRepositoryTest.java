package com.example.project.test;

import java.time.LocalDateTime;
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
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.repository.AdminMovieRepository;
import com.example.project.admin.repository.AdminRepository;
import com.example.project.admin.repository.MovieAddRepository;
import com.example.project.admin.repository.MovieStateRepository;
import com.example.project.entity.Genre;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.MovieGenreId;
import com.example.project.entity.MoviePerson;
import com.example.project.entity.Person;
import com.example.project.entity.constant.MemberRole;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.InquiryRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.movie.GenreRepository;
import com.example.project.repository.movie.MovieGenreRepository;
import com.example.project.repository.movie.MoviePersonRepository;
import com.example.project.repository.movie.MovieRepository;
import com.example.project.repository.movie.PersonRepository;
import com.example.project.repository.reserve.TheaterRepository;
import com.example.project.service.MovieService;
import com.querydsl.core.types.Predicate;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminMovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MoviePersonRepository moviePersonRepository;
    @Autowired
    private AdminMovieRepository adminMovieRepository;
    @Autowired
    private MovieAddRepository movieAddRepository;
    @Autowired
    private MovieStateRepository movieStateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    public void insertAdmin() {
        Admin admin = Admin.builder().userId("ADMIN").password(passwordEncoder.encode("1111"))
                .role(MemberRole.ADMIN).build();
        adminRepository.save(admin);
    }

    // @Test
    // public void userInsert() {
    // LongStream.rangeClosed(1, 50).forEach(i -> {
    // int re = ((int) (Math.random() * 2));
    // int year = ((int) (Math.random() * 50) + 1980);
    // int month = ((int) (Math.random() * 12) + 1);
    // int day = ((int) (Math.random() * 31) + 1);
    // UserEntity userEntity = UserEntity.builder()
    // .userId("userId" + i)
    // .password(passwordEncoder.encode("1111"))
    // .name("name" + i)
    // .email("usermeil" + i + "@gmail.com")
    // .gender(re)
    // .brith(year + "/" + month + "/" + day)
    // .telNo("010-0000-0000")
    // .adminRole(AdminRole.USER)
    // .statusRole(StatusRole.ACTIVE)
    // .build();

    // userRepository.save(userEntity);
    // });
    // }

    // @Test
    // public void findUser() {

    // System.out.println(userRepository.findById(1L));
    // }

    // @Test
    // public void userList() {
    // userRepository.findAll().forEach(u -> {
    // System.out.println(u.getUserId());
    // System.out.println(u.getName());
    // System.out.println(u.getEmail());
    // System.out.println(u.getGender());
    // });
    // ;
    // }

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

            // movie.getMoviePeople().forEach(people ->
            // System.out.println(people.getPerson().getName()));

            // movie.getMovieGenres().forEach(genre ->
            // System.out.println(genre.getGenre().getName()));
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
                .id(3L)
                .title("테스트중입니다")
                .overview("왜 안돼는가??")
                .releaseDate("2024-12-20")
                .build();
        movieRepository.save(movie);

        System.out.println(movie);

        // Genre genre = genreRepository.findById(28L).get(); // 장르 하나 추가
        List<Long> genreIds = List.of(12L, 27L, 37L); // 장르 여러개 추가
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("선택하신 장르를 찾을 수 없습니다. : " +
                            genreId)); // 선택한 장르가 없을때
            System.out.println(genre);

            MovieGenre movieGenre = MovieGenre.builder()
                    .id(MovieGenreId.builder()
                            .movie(movie)
                            .genre(genre)
                            .build())
                    .build();

            movieGenreRepository.save(movieGenre);
            System.out.println(movieGenre);
        }

    }

    @Commit
    @Transactional
    @Test
    public void addMovieWithActor() {

        Movie movie = movieRepository.findById(3L).get();

        System.out.println(movie);

        List<String> actors = List.of("김군", "이군", "박군"); // 새로운 배우 이름 리스트

        for (String actor : actors) {
            // 이름을 기준으로 배우 중복 여부 확인
            Person person = Person.builder()
                    .gender(1L) // 성별 설정
                    .job("Acting") // 직업 설정
                    .name(actor) // 이름 설정
                    .build();

            personRepository.save(person);

            // MoviePerson 생성
            MoviePerson moviePerson = MoviePerson.builder()
                    .movie(movie)
                    .person(person)
                    .build();

            // MoviePerson 저장
            moviePersonRepository.save(moviePerson);
            System.out.println("Saved MoviePerson: " + moviePerson);
        }
    }

    @Commit
    @Transactional
    @Test
    public void addMovieWithDirector() {

        Movie movie = movieRepository.findById(3L).get();

        System.out.println(movie);

        Person person = Person.builder()
                .gender(1L)
                .job("Directing") // 직업 설정
                .name("감독입니다") // 감독 이름 설정
                .build();

        personRepository.save(person);

        // MoviePerson 생성
        MoviePerson moviePerson = MoviePerson.builder()
                .movie(movie)
                .person(person)
                .build();

        // MoviePerson 저장
        moviePersonRepository.save(moviePerson);
        System.out.println("Saved MoviePerson: " + moviePerson);

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

    @Test
    public void ss() {
        List<MovieState> state = movieStateRepository.findAll();
        System.out.println(state);
    }

    @Test
    public void one() {
        MovieState state = movieStateRepository.findById(2L).get();
        System.out.println(state);
    }

    @Commit
    @Transactional
    @Test
    public void insetStateTest() {
        MovieState state = movieStateRepository.findById(3L).get();

        Theater movieAdd = Theater.builder()
                .theaterName("test test test")
                .theaterState("강원도")
                .theaterAdd("강원도")
                .movieState(state)
                .build();

        movieAddRepository.save(movieAdd);
        System.out.println(movieAdd);
    }

    @Test
    public void deleteTest() {
        movieAddRepository.deleteById(3051L);

    }

    @Test
    public void findLast() {
        System.out.println(memberRepository.findByLastLogin(1L));

    }

    @Test
    public void inquiry() {
        System.out.println(inquiryRepository.findById(1L));

    }

}
