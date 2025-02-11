package com.example.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.project.admin.filter.CustomSessionCookieFilter;
import com.example.project.admin.handler.CustomAuthenticationFailureHandler;
import com.example.project.admin.handler.CustomAuthenticationSuccessHandler;
import com.example.project.admin.service.AdminDetailsServiceImpl;
import com.example.project.service.MemberLoginServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

        @Autowired
        private CustomAuthenticationFailureHandler failureHandler;
        @Autowired
        private CustomAuthenticationSuccessHandler successHandler;
        @Autowired
        private AdminDetailsServiceImpl adminDetailsServiceImpl;
        @Autowired
        private MemberLoginServiceImpl memberLoginServiceImpl;
        @Autowired
        private CustomSessionCookieFilter sessionCookieFilter;

        @Order(2)
        @Bean
        SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {

                http
                                // 사용자 인증 서비스 설정
                                .userDetailsService(memberLoginServiceImpl)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/fonts/**", "/img/**",
                                                                "/js/**", "/sass/**", "/svg/**")
                                                .permitAll() // 정적 리소스 허용
                                                .requestMatchers("/review/submit").authenticated() // 리뷰 작성 요청은 인증 필요
                                                .requestMatchers("/review/**").authenticated() // 리뷰 관련 기타 요청 인증 필요
                                                .requestMatchers("/member/mypage").authenticated() // 마이페이지 접근은 인증 필요
                                                .requestMatchers("/mypage/reservations").authenticated() // 예매 내역 접근은 인증
                                                                                                         // 필요
                                                .requestMatchers("/movie/seat_sell?**").authenticated()
                                                .requestMatchers("/reservation/seat_sell/**").authenticated()
                                                .requestMatchers("/center/counseling/**", "/email-board/**")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.DELETE, "/member/**").authenticated()
                                                .requestMatchers("/payment-success").authenticated()
                                                .requestMatchers("/payment-history").authenticated()
                                                .requestMatchers("/cart/main").authenticated()
                                                .requestMatchers("/order").authenticated()
                                                .requestMatchers("/dormancy").permitAll()

                                                // 🔹 추가: 현재 로그인된 사용자 정보 조회 요청은 인증 필요
                                                .requestMatchers("/api/member/current").authenticated()

                                                // 🔹 그 외 요청은 모두 허용
                                                .anyRequest().permitAll())

                                // 로그인 설정
                                .formLogin(login -> login
                                                .loginPage("/member/login") // 사용자 정의 로그인 페이지
                                                .defaultSuccessUrl("/")
                                                .failureHandler(failureHandler)
                                                .successHandler(successHandler)
                                                .permitAll())

                                // 로그아웃 설정
                                .logout(logout -> logout
                                                .logoutUrl("/member/logout")
                                                .logoutSuccessUrl("/member/login")
                                                .invalidateHttpSession(true) // ✅ 세션 무효화
                                                .deleteCookies("JSESSIONID") // ✅ 쿠키 삭제
                                                .permitAll());

                // .sessionManagement(session -> session
                // .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                // .sessionFixation(
                // SessionManagementConfigurer.SessionFixationConfigurer::migrateSession))

                // .rememberMe(remember -> remember
                // .key("uniqueAndSecretForMember")
                // .rememberMeCookieName("MEMBER_REMEMBER_ME")
                // .tokenValiditySeconds(1209600) // 14일
                // );

                // CSRF 설정
                http.csrf(csrf -> csrf.disable()); // 필요에 따라 CSRF 비활성화

                return http.build();

        }

        @Order(1)
        @Bean
        SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {

                http
                                .securityMatcher("/admin/**")
                                .userDetailsService(adminDetailsServiceImpl)
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/css/**", "/fonts/**", "/img/**", "/js/**",
                                                                "/sass/**",
                                                                "/svg/**")
                                                .permitAll()
                                                .requestMatchers("/", "/admin/css/**",
                                                                "/admin/js/**", "/admin/fonts/**")
                                                .permitAll()
                                                .requestMatchers("/admin/page/**").hasAnyRole("ADMIN")
                                                .anyRequest().authenticated());

                // http.sessionManagement(session ->
                // session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

                http.formLogin(adminLogin -> adminLogin.loginPage("/admin/login")
                                .defaultSuccessUrl("/admin/page/index", true).permitAll());

                http.logout(logout -> logout
                                .logoutUrl("/admin/logout") // 로그아웃 요청 URL
                                .logoutSuccessUrl("/admin/login") // 로그아웃 성공 후 이동할 URL
                                .invalidateHttpSession(true) // 세션 무효화
                                .deleteCookies("JSESSIONID")); // 세션 쿠키 삭제
                // .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                // .logoutSuccessUrl("/admin/login"));

                // http.sessionManagement(session -> session
                // .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                // .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)

                // );

                // http.rememberMe(remember -> remember
                // .key("uniqueAndSecretForAdmin")
                // .rememberMeCookieName("ADMIN_REMEMBER_ME")
                // .tokenValiditySeconds(1209600) // 14일
                // );

                http.csrf(csrf -> csrf.disable());
                return http.build();

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

}
