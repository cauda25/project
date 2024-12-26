package com.example.project.service.test;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.dto.MovieDto;
import com.example.project.dto.test.UserDto;
import com.example.project.entity.Movie;
import com.example.project.entity.test.UserEntity;
import com.example.project.repository.MovieRepository;
import com.example.project.repository.test.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiecImpl implements UserServie{

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;


    @Override
    public List<UserEntity> allList(UserDto userDto) {
        List<UserEntity> list = userRepository.findAll();
     return list;
    }

    
    @Override
    public List<Movie> mList(MovieDto MovieDto) {
        List<Movie> list = movieRepository.findAll();
        return list;
    }
    
}
