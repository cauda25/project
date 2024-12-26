package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.People;

public interface PeopleRepository extends JpaRepository<People, Long> {

}
