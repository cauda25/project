package com.example.project.admin.dto.test;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class AuthUserDto extends User {

    private UserDto userDto;

    public AuthUserDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthUserDto(UserDto userDto) {
        this(userDto.getUserId(), userDto.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + userDto.getAdminRole())));
        this.userDto = userDto;
    }
}
