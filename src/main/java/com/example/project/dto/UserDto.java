package com.example.project.dto;

public class UserDto {

    private String username;
    private String email;
    private String mobile;

    public UserDto(String username, String email, String mobile) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

}
