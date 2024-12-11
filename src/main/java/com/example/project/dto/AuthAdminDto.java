package com.example.project.dto;

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
public class AuthAdminDto extends User{
    
    private AdminDto adminDto;

    public AuthAdminDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

    public AuthAdminDto(AdminDto adminDto){
        this(adminDto.getUserId(), adminDto.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_"+adminDto.getRole())));
        this.adminDto = adminDto;
    }
}
