package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/review/write").authenticated() // 리뷰 작성은 인증 필요
                        .anyRequest().permitAll() // 그 외 요청은 모두 허용
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청 URL
                        .logoutSuccessUrl("/member/login") // 로그아웃 후 이동할 URL
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                )
                // 추가적으로 CSRF 설정이나 기타 보안 설정을 추가 가능
                .csrf(csrf -> csrf.disable()); // 필요에 따라 CSRF 비활성화 // 세션 쿠키 삭제

        // http.authorizeHttpRequests(authorize -> authorize
        // .requestMatchers("/" ,"/admin/css/**",
        // "/admin/js/**","/admin/fonts/**").permitAll()
        // .requestMatchers( "/admin/page/**").hasRole("ADMIN")
        // // .anyRequest().permitAll()
        // // .requestMatchers("/admin/page/user").permitAll()
        // .anyRequest().authenticated()
        // );

        // http.formLogin(login -> login.loginPage("/admin/login")
        // .defaultSuccessUrl("/admin/page/index", true).permitAll());

        // http.sessionManagement(session ->
        // session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        // http.logout(logout -> logout
        // .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
        // .logoutSuccessUrl("/"));

        // http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
