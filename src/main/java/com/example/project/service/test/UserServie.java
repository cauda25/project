package com.example.project.service.test;

import java.util.List;

import com.example.project.dto.MovieDto;
import com.example.project.dto.test.UserDto;
import com.example.project.entity.Movie;
import com.example.project.entity.test.UserEntity;

public interface UserServie {


    List<UserEntity> allList(UserDto userDto);
    List<Movie> mList(MovieDto MovieDto);

    default UserEntity dtoToEntity(UserDto dto){
        return UserEntity.builder()
        .userid(dto.getUserid())
        .password(dto.getPassword())
        .name(dto.getName())
        .addr(dto.getAddr())
        .telNo(dto.getTelNo())
        .email(dto.getEmail())
        .point(dto.getPoint())
        .reser(dto.isReser())
        .gender(dto.getGender())
        .age(dto.getAge())
        .brith(dto.getBrith())
        .build();
    }
    default UserDto entityToDto(UserEntity entity){
        return UserDto.builder()
        .userid(entity.getUserid())
        .password(entity.getPassword())
        .name(entity.getName())
        .addr(entity.getAddr())
        .telNo(entity.getTelNo())
        .email(entity.getEmail())
        .point(entity.getPoint())
        .reser(entity.isReser())
        .gender(entity.getGender())
        .age(entity.getAge())
        .brith(entity.getBrith())
        .build();
    }
}
