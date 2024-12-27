package com.example.project.service;

import com.example.project.dto.PersonDto;

import java.util.List;

import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.PageResultDTO;
import com.example.project.entity.Person;

public interface PersonService {
    // 페이징된 인물 리스트
    PageResultDTO<PersonDto, Person> getList(PageRequestDTO requestDto);

    // 해당 인물의 상세 정보
    PersonDto read(Long id);

    // 해당 영화의 감독 리스트
    List<PersonDto> getDirectorListByMovieId(Long id);

    public default PersonDto entityToDto(Person people) {
        return PersonDto.builder()
                .id(people.getId())
                .gender(people.getGender())
                .job(people.getJob())
                .name(people.getName())
                .popularity(people.getPopularity())
                .profilePath(people.getProfilePath())
                .build();
    }

    public default Person dtoToEntity(PersonDto peopleDto) {
        return Person.builder()
                .id(peopleDto.getId())
                .gender(peopleDto.getGender())
                .job(peopleDto.getJob())
                .name(peopleDto.getName())
                .popularity(peopleDto.getPopularity())
                .profilePath(peopleDto.getProfilePath())
                .build();
    }
}
