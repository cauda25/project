package com.example.project.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonCustomRepository {

}
