package com.example.project.repository.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.People;

public interface PeopleRepository extends JpaRepository<People, Long>, PeopleCustomRepository {

}
