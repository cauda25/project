package com.example.project.repository.movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.project.entity.Person;

public interface PersonCustomRepository {
    // type, keyword에 따른 페이징된 인물 리스트
    Page<Person> getTotalList(String type, String keyword, Pageable pageable);

    // 해당 영화의 인물 리스트
    List<Person> getDirectorListByMovieId(Long id);
}
