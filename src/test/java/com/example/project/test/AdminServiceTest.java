package com.example.project.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import com.example.project.admin.Entity.MovieState;
import com.example.project.admin.dto.test.MovieStateDto;
import com.example.project.admin.service.test.UserService;
import com.example.project.service.MovieService;

@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void stateTest() {
        List<MovieStateDto> state = service.getAllStates();

        System.out.println(state);
    }
}
