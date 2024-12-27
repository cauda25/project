package com.example.project.admin.service.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.MovieState;
import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.dto.test.UserDto;
import com.example.project.admin.repository.AdminMovieRepository;
import com.example.project.admin.repository.MovieAddRepository;
import com.example.project.admin.repository.MovieStateRepository;
import com.example.project.admin.repository.UserRepository;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Genre;
import com.example.project.entity.Member;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.movie.GenreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServicecImpl implements UserService {

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;
    private final AdminMovieRepository adminMovieRepository;
    private final MemberRepository memberRepository;
    private final MovieAddRepository movieAddRepository;
    private final MovieStateRepository movieStateRepository;
    private final GenreRepository genreRepository;

    // 회원 정보 가져오기 test
    @Override
    public List<UserEntity> testList(UserDto userDto) {
        List<UserEntity> list = userRepository.findAll();

        return list;
    }

    // 회원 정보 가져오기
    @Override
    public List<Member> allList(MemberDto memberDto) {
        List<Member> list = memberRepository.findAll();
        return list;
    }

    // @Transactional
    // @Override
    // public List<MovieDto> getMovieDetailsWithGenres(Long id) {
    // // JPQL 쿼리 호출
    // List<Object[]> results = movieRepository.getMovieDetailsWithGenres(id);

    // // 결과를 저장할 리스트
    // List<MovieDto> details = new ArrayList<>();

    // for (Object[] result : results) {
    // Movie movie = (Movie) result[0]; // Movie 엔티티
    // Genre genre = (Genre) result[1]; // Genre 엔티티

    // // details.add(new MovieDetailsDTO(movie.getTitle(), movie.getReleaseDate(),
    // genre.getName()));
    // }

    // return details; // 리스트 반환
    // }

    // 영화 리스트 출력
    @Transactional
    @Override
    public List<MovieDetailsDTO> getMovieDetails() {
        List<Object[]> results = adminMovieRepository.getMovieDetails();
        List<MovieDetailsDTO> movieDetailsList = new ArrayList<>();

        for (Object[] result : results) {
            Long mid = (Long) result[0];
            String title = (String) result[1];
            String genres = (String) result[2];
            String releaseDate = (String) result[3];

            movieDetailsList.add(new MovieDetailsDTO(mid, title, releaseDate, genres));
        }

        return movieDetailsList;
    }

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
        LocalDateTime lastTime = LocalDateTime.now().minusMinutes(1);
        List<UserEntity> inactiveUsers = userRepository.findInactiveUsers(lastTime, StatusRole.ACTIVE);

        for (UserEntity userEntity : inactiveUsers) {
            userEntity.setStatusRole(StatusRole.INACTIVE);

            userRepository.save(userEntity);
        }
    }

    // 휴면 계정 복구
    @Override
    public void reactivateAccount(Long uno) {
        UserEntity userEntity = userRepository.findById(uno).get();

        if (!userEntity.getStatusRole().equals(StatusRole.INACTIVE)) {
            throw new IllegalStateException("비활성 상태가 아닙니다.");
        }

        userEntity.setStatusRole(StatusRole.ACTIVE);
        userEntity.setLastLogin(LocalDateTime.now());
        userRepository.save(userEntity);

    }

    // 키 값을 찾아오기
    @Override
    public UserEntity findUsername(String username) {
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

}
