package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import com.example.project.admin.service.AdminDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                // 권한 설정
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/admin/**",
                                                                "/fonts/**", "/img/**",
                                                                "/js/**", "/sass/**", "/svg/**")
                                                .permitAll() // 정적 리소스는 모두 허용
                                                .requestMatchers("/review/submit").authenticated() // 리뷰 작성 요청은 인증 필요
                                                .requestMatchers("/review/**").authenticated() // 리뷰 관련 기타 요청 인증 필요
                                                .requestMatchers("/member/mypage").authenticated() // 마이페이지 접근은 인증 필요
                                                .requestMatchers("/mypage/reservations").authenticated() // 예매 내역 접근은 인증
                                                // 필요
                                                .anyRequest().permitAll() // 그 외 요청은 모두 허용
                                )
                                // 로그인 설정
                                .formLogin(login -> login
                                                .loginPage("/member/login") // 사용자 정의 로그인 페이지 URL
                                                .defaultSuccessUrl("/movie/main", true) // 로그인 성공 시 이동할 URL
                                                .permitAll() // 로그인 페이지는 인증 없이 접근 가능
                                )
                                // 로그아웃 설정
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // 로그아웃 요청 URL
                                                .logoutSuccessUrl("/member/login") // 로그아웃 성공 후 이동할 URL
                                                .invalidateHttpSession(true) // 세션 무효화
                                                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                                )
                                // CSRF 설정
                                .csrf(csrf -> csrf.disable()); // 필요에 따라 CSRF 비활성화

                // http.authorizeHttpRequests(authorize -> authorize
                // .requestMatchers("/", "/admin/css/**",
                // "/admin/js/**", "/admin/fonts/**")
                // .permitAll()
                // .requestMatchers("/admin/page/**").hasRole("ADMIN")
                // // .anyRequest().permitAll()
                // // .requestMatchers("/admin/page/user").permitAll()
                // .anyRequest().authenticated());

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
