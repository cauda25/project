package com.example.project.admin.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.project.admin.Entity.constant.StatusRole;

import com.example.project.admin.service.test.UserService;
import com.example.project.entity.Member;
import com.example.project.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        // 로그인 시도 시 입력된 값 요청 후 파라메터에서 가져오기
        Member user = userService.findUsername(request.getParameter("username"));

        String redirectUrl = "/member/login";

        if (user != null && user.getStatusRole() == StatusRole.INACTIVE) {
            redirectUrl = "/dormancy?mid=" + user.getMid(); // 비활성화 상태 사용자 처리
        }

        response.sendRedirect(redirectUrl);

        // INACTIVE를 ACTIVE로 변경전 미로그인 상태였기 때문에 UNO 등 값을 못 가져옴
        // 결과 리다이렉션 실패 아무것도 수행하지 못한 상태로 LOGIN 페이지로 돌아옴
        // String username = request.getParameter("username");
        // String redirectUrl = "/admin/login?error=true";

        // // 사용자 존재 여부 확인
        // Optional<UserEntity> userOptional = userRepository.findByUserId(username);
        // if (userOptional.isPresent()) {
        // UserEntity user = userOptional.get();

        // // INACTIVE 상태라면 다른 경로로 리디렉션
        // if (user.getStatusRole() == StatusRole.INACTIVE) {
        // redirectUrl = "/dormancy";
        // }
        // }

        // // 로그인 실패 시 리디렉션
        // getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
