package com.example.project.admin.service.test;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import com.example.project.admin.Entity.MovieState;
import com.example.project.dto.GenreDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.admin.dto.test.AdminInquiryDto;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;

import com.example.project.entity.Genre;
import com.example.project.entity.Inquiry;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.User;

public interface UserService {
    // 회원 정보 리스트 테스트
    // List<Member> testList(MemberDto memberDto);
    // 키 값 찾기
    Member findUsername(String memberId);

    // 로그인 최종 기록 보여주기
    List<Member> findLastLogin(Long mid);

    // 회원 정보 리스트
    List<Member> allList(MemberDto memberDto);

    // 영화 정보(제목,장르,개봉일) 리스트
    // List<MovieDetailsDTO> getMovieDetails();
    PageResultDTO<MovieDetailsDTO, Object[]> getMovieDetails(PageRequestDTO requestDto);

    // 영화 정보(배우) 리스트
    List<MovieDetailsDTO> movieActers();

    // 영화 정보(감독) 리스트
    List<MovieDetailsDTO> movieDirector();

    // 영화 등록
    void addMovieWithDetails(Movie movie, List<Long> genreIds, List<String> actors, String directorName);

    // 영화관 지역선택 또는 영화관명 검색으로 리스트 출력
    List<TheaterDto> selectList(String state, String theaterName);

    // 영화 장르 리스트
    List<GenreDto> getAllGenres();

    // 지역 select 리스트
    List<MovieStateDto> getAllStates();

    // 영화관 등록
    Long addMovie(TheaterDto aDto);

    // 영화관 삭제
    void delete(Long theaterId);

    // 휴면 계정 전환
    // @Scheduled(cron = "0/10 0 0 * * *")
    void inactiveAccounts();

    // 휴면 계정 복구
    void reactivateAccount(Long mid);

    // 이메일 문의 가져오기
    List<Inquiry> inquityList(AdminInquiryDto adminInquiryDto);

    // 이메일 상세 문의 가져오기
    Inquiry getInquity(Long id);

    // 이메일 답변
    void insertInquity(Inquiry inquiry, String answer);

}
