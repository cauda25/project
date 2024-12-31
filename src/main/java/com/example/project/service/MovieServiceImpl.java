package com.example.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.entity.Genre;
import com.example.project.entity.MemberFavoriteMovie;
import com.example.project.entity.Movie;
import com.example.project.entity.MovieGenre;
import com.example.project.entity.MoviePerson;
import com.example.project.entity.Person;
import com.example.project.repository.MemberFavoriteMovieRepository;
import com.example.project.repository.movie.MovieGenreRepository;
import com.example.project.repository.movie.MoviePersonRepository;
import com.example.project.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MemberFavoriteMovieRepository favoriteMoviesRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MoviePersonRepository moviePersonRepository;

    @Override
    public PageResultDTO getList(PageRequestDTO requestDto) {
        // 페이지 나누기 개념 추가
        Pageable pageable = requestDto.getPageable(Sort.by(requestDto.getSort()).descending());
        Page<Object[]> movies = movieRepository.getTotalList(requestDto.getType(),
                requestDto.getKeyword(),
                requestDto.getMovieList(),
                requestDto.getGenre(), pageable);
        Function<Object[], MovieDto> function = (en -> entityToDto((Movie) en[0],
                (List<MoviePerson>) en[1],
                (List<Person>) en[2],
                (List<Genre>) en[3]));

        return new PageResultDTO<>(movies, function);
    }

    // @Override
    // public MovieDto read(Long id) {
    // return null;
    // // return entityToDto(movieRepository.findById(id).get());
    // }

    // @Override
    // public List<String> getDirectorList(Long id) {
    // return movieRepository.getDirectorList(id);
    // }

    // @Override
    // public List<String> getActorList(Long id) {
    // return movieRepository.getActorList(id);
    // }

    // @Override
    // public List<String> getGenreList(Long id) {
    // return movieRepository.getGenreList(id);
    // }

    @Override
    public List<MovieDto> getMovieListByPersonId(Long id) {
        List<MovieDto> movieDtos = new ArrayList<>();
        movieRepository.getMovieListByPersonId(id).stream().forEach(movie -> {
            movieDtos.add(entityToDto(movie, null, null, null));
        });
        return movieDtos;
    }

    @Override
    public MovieDto getMovieDetail(Long id) {
        Object[] result = movieRepository.getMovieDetailById(id);
        return entityToDto((Movie) result[0],
                (List<MoviePerson>) result[1],
                (List<Person>) result[2],
                (List<Genre>) result[3]);
    }

    @Override
    public List<MovieDto> getFavoriteMoviesByMemberId(Long memberId) {
        return favoriteMoviesRepository.findByMemberId(memberId).stream()
                .map(movie -> {
                    Object[] result = movieRepository.getMovieDetailById(movie.getMovie().getId());
                    return entityToDto((Movie) result[0],
                            (List<MoviePerson>) result[1],
                            (List<Person>) result[2],
                            (List<Genre>) result[3]);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> recommendMovies(Long memberId) { // 찜한 영화 리스트 가져오기
        List<MemberFavoriteMovie> favoriteMovies = favoriteMoviesRepository.findByMemberId(memberId);

        // 찜한 영화들로부터 장르 추출
        List<Genre> genres = new ArrayList<>();
        for (MemberFavoriteMovie favoriteMovie : favoriteMovies) {
            Movie movie = favoriteMovie.getMovie();
            List<MovieGenre> movieGenres = movieGenreRepository.findByMovieId(movie.getId());
            for (MovieGenre movieGenre : movieGenres) {
                genres.add(movieGenre.getGenre());
            }
            // 영화에 관련된 장르들을 모두 추출
        }
        if (!genres.isEmpty()) {
            // 장르에 기반하여 추천 영화 리스트 가져오기
            List<Movie> recommendedMovies = movieRepository.findMoviesByGenres(genres);

            // Movie 객체를 MovieDto로 변환
            List<MovieDto> movieDtos = recommendedMovies.stream()
                    .map(movie -> entityToDto(movie, null, null, genres))
                    .collect(Collectors.toList());
            return movieDtos;
        } else {
            List<MovieDto> movieDtos = new ArrayList<>();
            return movieDtos;
        }

    }

    @Override
    public Long findMostFrequentDirector(Long memberId) {
        List<MemberFavoriteMovie> favoriteMovies = favoriteMoviesRepository.findByMemberId(memberId);

        Map<Long, Integer> directorMovieCount = new HashMap<>();
        for (MemberFavoriteMovie favoriteMovie : favoriteMovies) {
            Movie movie = favoriteMovie.getMovie();
            List<MoviePerson> moviePeople = moviePersonRepository.findByMovieId(movie.getId());
            for (MoviePerson moviePerson : moviePeople) {
                Person person = moviePerson.getPerson();
                if (person.getJob().equals("Directing")) {
                    Long directorId = person.getId();
                    directorMovieCount.put(directorId, directorMovieCount.getOrDefault(directorId, 0) + 1);
                }
            }
        }
        // 3. 가장 많이 찜한 감독 찾기
        return directorMovieCount.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        // if (mostFrequentDirectorId == null) {
        // return Collections.emptyList(); // 감독이 없으면 빈 리스트 반환
        // }

        // // 4. 해당 감독의 영화 리스트 찾기
        // List<Movie> recommendedMovies =
        // movieRepository.findMoviesByDirectorId(mostFrequentDirectorId);

        // return recommendedMovies.stream()
        // .map(movie -> {
        // Object[] result = movieRepository.getMovieDetailById(movie.getId());
        // return entityToDto((Movie) result[0],
        // (List<MoviePerson>) result[1],
        // (List<Person>) result[2],
        // (List<Genre>) result[3]);
        // })
        // .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> similarMovies(Long id) {
        Movie movie = movieRepository.findById(id).get();
        List<Genre> genres = new ArrayList<>();
        List<MovieGenre> movieGenres = movieGenreRepository.findByMovieId(movie.getId());
        for (MovieGenre movieGenre : movieGenres) {
            genres.add(movieGenre.getGenre());
        }
        // 장르에 기반하여 추천 영화 리스트 가져오기
        List<Movie> recommendedMovies = movieRepository.findMoviesByGenres(genres);

        // Movie 객체를 MovieDto로 변환
        List<MovieDto> movieDtos = recommendedMovies.stream()
                .map(m -> entityToDto(m, null, null, genres))
                .collect(Collectors.toList());

        return movieDtos;
    }

}
