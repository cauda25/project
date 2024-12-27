package com.example.project.admin.service.test;

import java.util.List;

import com.example.project.admin.Entity.MovieState;
import com.example.project.dto.MemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.dto.reserve.TheaterDto;
import com.example.project.admin.dto.test.MovieDetailsDTO;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.entity.Genre;
import com.example.project.entity.Member;
import com.example.project.entity.Movie;
import com.example.project.entity.test.UserEntity;

public interface UserService {
    // 회원 정보 리스트
    List<Member> allList(MemberDto memberDto);

    // 영화 정보(제목,장르,개봉일) 리스트
    List<MovieDetailsDTO> getMovieDetails();

    // 영화 정보(배우) 리스트
    List<MovieDetailsDTO> movieActers();

    // 영화관 지역선택 또는 영화관명 검색으로 리스트 출력
    List<TheaterDto> selectList(String state, String theaterName);

    // 지역 select 리스트
    List<MovieStateDto> getAllStates();

    // 영화관 등록
    Long addMovie(TheaterDto aDto);

    // 영화관 삭제
    void delete(Long theaterId);

}
