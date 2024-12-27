package com.example.project.repository.test;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.test.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    
}
