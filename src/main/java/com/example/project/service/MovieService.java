package com.example.project.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.project.dto.GenreDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.MoviePersonDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.PersonDto;
import com.example.project.entity.Genre;
import com.example.project.entity.Movie;
import com.example.project.entity.MoviePerson;
import com.example.project.entity.Person;

public interface MovieService {
    // 페이징된 영화 리스트
    PageResultDTO<MovieDto, Movie> getList(PageRequestDTO requestDto);

    // 해당 인물의 영화 리스트
    List<MovieDto> getMovieListByPersonId(Long id);

    // 해당 영화의 상세 정보
    MovieDto getMovieDetail(Long id);

    // 해당 멤버의 찜한 영화 리스트
    List<MovieDto> getFavoriteMoviesByMemberId(Long memberId);

    // 해당 멤버의 찜한 영화 리스트의 장르를 기반으로 한 영화 리스트
    List<MovieDto> recommendMovies(Long memberId);

    // 해당 멤버의 찜한 영화 리스트의 감독 중 가장 중복된 감독
    Long findMostFrequentDirector(Long memberId);

    // 해당 영화와 같은 장르의 영화 리스트
    List<MovieDto> similarMovies(Long id);

    // List<String> getDirectorList(Long id);

    // List<String> getActorList(Long id);

    // List<String> getGenreList(Long id);

    public default MovieDto entityToDto(Movie movie, List<MoviePerson> moviepeople, List<Person> people,
            List<Genre> genres) {
        // MovieDto 생성
        MovieDto movieDto = MovieDto.builder()
                .id(movie.getId())
                .backdrop_path(movie.getBackdrop_path())
                .budget(movie.getBudget())
                .homepage(movie.getHomepage())
                .originalLanguage(movie.getOriginalLanguage())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .popularity(movie.getPopularity())
                .posterPath(movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .revenue(movie.getRevenue())
                .runtime(movie.getRuntime())
                .status(movie.getStatus())
                .tagline(movie.getTagline())
                .title(movie.getTitle())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .build();

        // MoviePeopleDto 생성 (movieDto와 peopleId 설정)
        if (moviepeople != null) {
            List<MoviePersonDto> moviePersonDtos = moviepeople.stream().map(moviePerson -> {
                return MoviePersonDto.builder()
                        .id(moviePerson.getId())
                        .character(moviePerson.getCharacter())
                        .role(moviePerson.getRole())
                        .movieId(movie.getId()) // movieId 설정
                        .personId(moviePerson.getPerson().getId()) // peopleId 설정
                        .build();
            }).collect(Collectors.toList());

            // PeopleDto 생성 (moviePersonDtos 포함)
            if (people != null) {
                List<PersonDto> personDtos = people.stream().map(person -> {
                    return PersonDto.builder()
                            .id(person.getId())
                            .gender(person.getGender())
                            .job(person.getJob())
                            .name(person.getName())
                            .popularity(person.getPopularity())
                            .profilePath(person.getProfilePath())
                            .moviePersonDtos(moviePersonDtos.stream()
                                    .filter(moviePeopleDto -> moviePeopleDto
                                            .getPersonId()
                                            .equals(person.getId())) // 해당
                                                                     // peopleId에
                                                                     // 맞는
                                                                     // moviePeopleDto만
                                                                     // 필터링
                                    .collect(Collectors.toList())) // 필터링된
                                                                   // moviePersonDtos
                                                                   // 설정
                            .build();
                }).collect(Collectors.toList());

                movieDto.setPersonDtos(personDtos);
                movieDto.setMoviePersonDtos(moviePersonDtos);
            }
        }

        if (genres != null) {

            // GenreDto 생성
            List<GenreDto> genreDtos = genres.stream().map(genre -> {
                return GenreDto.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build();
            }).collect(Collectors.toList());
            movieDto.setGenreDtos(genreDtos);
        }

        // movieDto에 관련된 정보 설정

        return movieDto;
    }

    public default Movie dtoToEntity(MovieDto movieDto) {
        return Movie.builder()
                .id(movieDto.getId())
                .backdrop_path(movieDto.getBackdrop_path())
                .budget(movieDto.getBudget())
                .homepage(movieDto.getHomepage())
                .originalLanguage(movieDto.getOriginalLanguage())
                .originalTitle(movieDto.getOriginalTitle())
                .overview(movieDto.getOverview())
                .popularity(movieDto.getPopularity())
                .posterPath(movieDto.getPosterPath())
                .releaseDate(movieDto.getReleaseDate())
                .revenue(movieDto.getRevenue())
                .runtime(movieDto.getRuntime())
                .status(movieDto.getStatus())
                .tagline(movieDto.getTagline())
                .title(movieDto.getTitle())
                .voteAverage(movieDto.getVoteAverage())
                .voteCount(movieDto.getVoteCount())
                .build();
    }
}