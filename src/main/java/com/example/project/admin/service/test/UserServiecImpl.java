package com.example.project.admin.service.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.MovieState;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.dto.test.AdminInquiryDto;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.repository.AdminMovieRepository;
import com.example.project.admin.repository.MovieAddRepository;
import com.example.project.admin.repository.MovieStateRepository;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Genre;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.MovieGenreId;
import com.example.project.entity.MoviePerson;
import com.example.project.entity.Person;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.InquiryRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.movie.GenreRepository;
import com.example.project.repository.movie.MovieGenreRepository;
import com.example.project.repository.movie.MoviePersonRepository;
import com.example.project.repository.movie.MovieRepository;
import com.example.project.repository.movie.PersonRepository;

import groovyjarjarantlr4.v4.parse.ANTLRParser.ruleEntry_return;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiecImpl implements UserService {

    private final UserDetailsService userDetailsService;

    // private final UserRepository userRepository;
    private final AdminMovieRepository adminMovieRepository;
    private final MemberRepository memberRepository;

    private final MovieAddRepository movieAddRepository;
    private final MovieStateRepository movieStateRepository;

    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final GenreRepository genreRepository;
    private final MoviePersonRepository moviePersonRepository;
    private final PersonRepository personRepository;

    private final InquiryRepository inquiryRepository;

    // 회원 정보 가져오기 test
    // @Override
    // public List<Member> testList(UserDto userDto)
    // List<Member> list = userRepository.findAll();
    // return list;
    // }

    // 회원 정보 가져오기
    @Override
    public List<Member> allList(MemberDto memberDto) {
        List<Member> list = memberRepository.findAll();
        return list;
    }

    // 영화 리스트 출력
    @Transactional
    @Override
    public PageResultDTO<MovieDetailsDTO, Object[]> getMovieDetails(PageRequestDTO requestDto) {
        Pageable pageable = requestDto.getPageable(Sort.by("release_date").descending()); // 기본 정렬 추가 가능

        Page<Object[]> results = adminMovieRepository.getMovieDetails(pageable);

        Function<Object[], MovieDetailsDTO> function = (result -> new MovieDetailsDTO(
                (Long) result[0], // mid
                (String) result[1], // title
                (String) result[3], // releaseDate
                (String) result[2] // genres
        ));

        return new PageResultDTO<>(results, function);
    }
    // public List<MovieDetailsDTO> getMovieDetails() {
    // List<Object[]> results = adminMovieRepository.getMovieDetails();
    // List<MovieDetailsDTO> movieDetailsList = new ArrayList<>();

    // for (Object[] result : results) {
    // Long mid = (Long) result[0];
    // String title = (String) result[1];
    // String genres = (String) result[2];
    // String releaseDate = (String) result[3];

    // movieDetailsList.add(new MovieDetailsDTO(mid, title, releaseDate, genres));
    // }

    // return movieDetailsList;
    // }

    // 영화 배우리스트 출력
    @Override
    public List<MovieDetailsDTO> movieActers() {

        List<Object[]> results = adminMovieRepository.movieActers();
        List<MovieDetailsDTO> movieActorsList = new ArrayList<>();

        for (Object[] result : results) {
            Long mid = (Long) result[0];
            String title = (String) result[1];
            String name = (String) result[2];

            movieActorsList.add(new MovieDetailsDTO(mid, title, name));
        }

        return movieActorsList;
    }

    // 영화 감독리스트 출력
    @Override
    public List<MovieDetailsDTO> movieDirector() {

        List<Object[]> results = adminMovieRepository.movieDirector();
        List<MovieDetailsDTO> movieDirectorList = new ArrayList<>();

        for (Object[] result : results) {
            Long mid = (Long) result[0];
            String title = (String) result[1];
            String name = (String) result[2];

            movieDirectorList.add(new MovieDetailsDTO(mid, title, name));
        }

        return movieDirectorList;

    }

    // 영화관 지역 또는 관명으로 검색
    @Transactional
    @Override
    public List<TheaterDto> selectList(String state, String theaterName) {
        List<Object[]> addList = movieAddRepository.stateAndName(state, theaterName);
        return addList.stream()
                .map(result -> {
                    Theater theater = (Theater) result[0];
                    MovieState movieState = (MovieState) result[1];
                    return new TheaterDto(theater.getTheaterId(), theater.getTheaterName(), theater.getTheaterAdd(),
                            movieState.getState());
                })
                .collect(Collectors.toList());
    }

    // 영화관 지역 출력
    @Override
    public List<MovieStateDto> getAllStates() {
        return movieStateRepository.findAll()
                .stream()
                .map(state -> new MovieStateDto(state.getSno(), state.getState()))
                .collect(Collectors.toList());
    }

    // 영화 장르 출력
    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }

    // 영화관 리스트 출력
    @Transactional
    @Override
    public Long addMovie(TheaterDto aDto) {
        MovieState movieState = movieStateRepository.findById(aDto.getSno()).get();

        Theater theater = Theater.builder()
                .theaterName(aDto.getTheaterName())
                .theaterAdd(aDto.getTheaterAdd())
                .theaterState(aDto.getTheaterState())
                .movieState(movieState)
                .build();

        return movieAddRepository.save(theater).getTheaterId();
    }

    // 영화관 정보 삭제
    @Override
    public void delete(Long theaterId) {
        movieAddRepository.deleteById(theaterId);
    }

    // 휴면 계정으로 전환
    @Override
    public void inactiveAccounts() {
        LocalDateTime lastTime = LocalDateTime.now().minusDays(2);
        List<Member> inactiveUsers = memberRepository.findInactiveUsers(lastTime, StatusRole.ACTIVE);

        for (Member member : inactiveUsers) {
            member.setStatusRole(StatusRole.INACTIVE);

            memberRepository.save(member);
        }
    }

    // 휴면 계정 복구
    @Override
    public void reactivateAccount(Long mid) {
        Member member = memberRepository.findById(mid).get();

        if (!member.getStatusRole().equals(StatusRole.INACTIVE)) {
            throw new IllegalStateException("비활성 상태가 아닙니다.");
        }

        member.setStatusRole(StatusRole.ACTIVE);
        member.setLastLogin(LocalDateTime.now());
        memberRepository.save(member);

    }

    // 키 값을 찾아오기
    @Override
    public Member findUsername(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<Member> findLastLogin(Long mid) {
        return memberRepository.findByLastLogin(mid);
    }

    @Transactional
    @Override
    public void addMovieWithDetails(Movie movie, List<Long> genreIds, List<String> actors, String directorName) {
        // 1. 영화 저장
        movieRepository.save(movie);
        log.info("Saved Movie: {}" + movie);

        // 2. 장르 추가
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("선택하신 장르를 찾을 수 없습니다. : " + genreId));
            MovieGenre movieGenre = MovieGenre.builder()
                    .id(MovieGenreId.builder().movie(movie).genre(genre).build())
                    .build();
            movieGenreRepository.save(movieGenre);
            log.info("Saved MovieGenre: {}" + movieGenre);
        }

        // 3. 배우 추가
        for (String actor : actors) {
            Person actorPerson = Person.builder()
                    .job("Acting")
                    .name(actor)
                    .build();
            personRepository.save(actorPerson);
            MoviePerson movieActor = MoviePerson.builder()
                    .movie(movie)
                    .person(actorPerson)
                    .build();
            moviePersonRepository.save(movieActor);
            log.info("Saved MovieActor: {}" + movieActor);
        }

        // 4. 감독 추가
        Person directorPerson = Person.builder()
                .job("Directing")
                .name(directorName)
                .build();
        personRepository.save(directorPerson);
        MoviePerson movieDirector = MoviePerson.builder()
                .movie(movie)
                .person(directorPerson)
                .build();
        moviePersonRepository.save(movieDirector);
        log.info("Saved MovieDirector: {}" + movieDirector);
    }

    @Override
    public List<Inquiry> inquityList(AdminInquiryDto adminInquiryDto) {
        List<Inquiry> list = inquiryRepository.findAll();
        return list;
    }

    @Override
    public Inquiry getInquity(Long id) {
        return inquiryRepository.findById(id).get();

    }

    @Override
    public void insertInquity(Inquiry inquiry, String answer) {
        inquiry.setAnswer(answer);
        inquiryRepository.save(inquiry);
    }
}
