package com.example.project.admin.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.MovieState;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.repository.AdminMovieRepository;
import com.example.project.admin.repository.MovieAddRepository;
import com.example.project.admin.repository.MovieStateRepository;
import com.example.project.dto.MemberDto;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.entity.Member;
import com.example.project.entity.reserve.Theater;
import com.example.project.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServicecImpl implements UserService {

    // private final UserRepository userRepository;
    private final AdminMovieRepository adminMovieRepository;
    private final MemberRepository memberRepository;
    private final MovieAddRepository movieAddRepository;
    private final MovieStateRepository movieStateRepository;

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
            String title = (String) result[0];
            String genres = (String) result[1];
            String releaseDate = (String) result[2];

            movieDetailsList.add(new MovieDetailsDTO(title, releaseDate, genres));
        }

        return movieDetailsList;
    }

    // 영화 배우리스트 출력
    @Override
    public List<MovieDetailsDTO> movieActers() {
        List<Object[]> results = adminMovieRepository.movieActers();
        List<MovieDetailsDTO> movieActorsList = new ArrayList<>();

        for (Object[] result : results) {
            String title = (String) result[0];
            String name = (String) result[1];

            movieActorsList.add(new MovieDetailsDTO(title, name));
        }

        return movieActorsList;
    }
    // @Override
    // public List<MovieAddDto> addList() {
    // List<Object[]> addList = movieAddRepository.findByAddList();
    // List<MovieAddDto> movieAdds = new ArrayList<>();

    // for (Object[] objects : addList) {
    // String state = (String) objects[0];
    // String name = (String) objects[1];
    // String add = (String) objects[2];
    // log.info("add :", add);
    // movieAdds.add(new MovieAddDto(name, add, state));
    // }
    // log.info("영화관 상세 ", movieAdds);

    // return movieAdds;
    // }

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

}
