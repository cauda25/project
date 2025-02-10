package com.example.project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import com.example.project.repository.movie.PersonRepository;

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
    private final PersonRepository personRepository;

    private static final String API_KEY = "a7e035c352858d4f14b0213f9415827c";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

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

    // 서버 실행 시 실행
    // @EventListener(ApplicationReadyEvent.class)
    // 매일 0시 실행
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void insertAndUpdateMovies() {
        // 해당 페이지 범위에서 영화 데이터를 가져와서 처리
        IntStream.rangeClosed(1, 5).forEach(i -> {
            try {
                // Popular 영화 목록 데이터를 가져오기
                JSONObject jsonObject = fetchData(
                        BASE_URL + "movie/popular?language=ko-KR&region=KR&page=" + i + "&api_key=" + API_KEY);
                // 영화 목록에서 'results' 데이터를 가져옴
                JSONArray results = (JSONArray) jsonObject.get("results");

                // 각 영화에 대해 상세 데이터를 가져오고 처리
                results.forEach(array -> {
                    try {
                        // 한 영화의 기본 정보를 가져옴
                        JSONObject discoverMovie = (JSONObject) array;

                        // 영화 상세 정보 가져오기 (상세 페이지)
                        Long movieID = (Long) discoverMovie.get("id");
                        JSONObject movieDetails = fetchData(
                                BASE_URL + "movie/" + movieID + "?language=ko-KR&api_key=" + API_KEY);

                        // Movie 객체로 변환하여 저장
                        Movie movie = mapToMovie(movieDetails);
                        movieRepository.save(movie);

                        // 영화 장르 데이터를 처리
                        processMovieGenres(discoverMovie, movie);

                        // 영화 관련 인물 데이터 (출연 배우, 감독 등) 처리
                        processMoviePeople(movieID, movie);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Fetch JSON 데이터를 처리하는 메서드
    private JSONObject fetchData(String url) {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> entity = rt.getForEntity(url, String.class);
        try {
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(entity.getBody());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse JSON for URL: " + url, e);
        }
    }

    // Movie 데이터를 맵핑하는 메서드
    private Movie mapToMovie(JSONObject movieDetails) {
        return Movie.builder()
                .id((Long) movieDetails.get("id"))
                .backdropPath((String) movieDetails.get("backdrop_path"))
                .budget((Long) movieDetails.get("budget"))
                .homepage((String) movieDetails.get("homepage"))
                .originalLanguage((String) movieDetails.get("original_language"))
                .originalTitle((String) movieDetails.get("original_title"))
                .overview((String) movieDetails.get("overview"))
                .popularity((Double) movieDetails.get("popularity"))
                .posterPath((String) movieDetails.get("poster_path"))
                .releaseDate((String) movieDetails.get("release_date"))
                .revenue((Long) movieDetails.get("revenue"))
                .runtime((Long) movieDetails.get("runtime"))
                .status((String) movieDetails.get("status"))
                .tagline((String) movieDetails.get("tagline"))
                .title((String) movieDetails.get("title"))
                .voteAverage((Double) movieDetails.get("vote_average"))
                .voteCount((Long) movieDetails.get("vote_count"))
                .build();
    }

    // 영화 장르 데이터를 처리하는 메서드
    private void processMovieGenres(JSONObject discoverMovie, Movie movie) {
        try {
            JSONArray genres = (JSONArray) discoverMovie.get("genre_ids");
            genres.forEach(genreId -> {
                MovieGenre movieGenre = MovieGenre.builder()
                        .movie(Movie.builder().id(movie.getId()).build())
                        .genre(Genre.builder().id((Long) genreId).build())
                        .build();
                movieGenreRepository.save(movieGenre);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 영화 인물 데이터를 처리하는 메서드
    private void processMoviePeople(Long movieID, Movie movie) {
        try {
            JSONObject credits = fetchData(
                    BASE_URL + "movie/" + movieID + "/credits?language=ko-KR&api_key=" + API_KEY);

            // Crew 데이터 처리
            JSONArray crew = (JSONArray) credits.get("crew");
            processPeople(crew, movie, "job", false);

            // Cast 데이터 처리
            JSONArray cast = (JSONArray) credits.get("cast");
            processPeople(cast, movie, "character", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 인물 데이터를 공통적으로 처리하는 메서드
    private void processPeople(JSONArray peopleArray, Movie movie, String roleKey, boolean isCast) {
        peopleArray.forEach(pInfo -> {
            try {
                JSONObject p = (JSONObject) pInfo;
                Long id = (Long) p.get("id");
                Long gender = (Long) p.get("gender");
                String name = (String) p.get("name");
                Double popularity = (Double) p.get("popularity");
                String profilePath = (String) p.get("profile_path");
                String role = isCast ? (String) p.get("character") : (String) p.get(roleKey);
                String knownForDepartment = (String) p.get("known_for_department");

                // Person 데이터 저장
                if (knownForDepartment.equals("Acting") || knownForDepartment.equals("Directing")) {
                    Person person = Person.builder()
                            .id(id)
                            .gender(gender)
                            .job(knownForDepartment)
                            .name(name)
                            .popularity(popularity)
                            .profilePath(profilePath)
                            .build();
                    personRepository.save(person);

                    // 저장
                    MoviePerson moviePerson = MoviePerson.builder()
                            .movie(Movie.builder().id(movie.getId()).build())
                            .person(Person.builder().id(id).build())
                            .build();
                    if (isCast) {
                        moviePerson.setCharacter(role);
                        moviePerson.setRole("Actor");
                    } else {
                        moviePerson.setRole(role);
                    }
                    moviePersonRepository.save(moviePerson);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
