package com.example.project.dto.test;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String userid;
    private String password;

    private String name;

    private String addr;
    
    private String telNo;

    private String email;

    private int point;

    private boolean reser;

    private int gender;
    
    private int age;
    private String brith;
}
