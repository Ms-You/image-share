package com.share.image.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Transactional
public class AccountLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    String errorMsg = null;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof LockedException){
            errorMsg = "정지된 계정입니다. 자세한 사항은 관리자에게 문의해주세요.";
        } else if (exception instanceof BadCredentialsException){
            errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else{
            errorMsg = "알수없는 이유로 로그인에 실패하였습니다.";
        }

        request.setAttribute("errorMsg", errorMsg);
        request.getRequestDispatcher("/auth/login").forward(request, response);
    }


}
