package com.share.image.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class AccountLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();


        if (principal.getUser().getTemporaryLocked().equals("해제하기") || principal.getUser().getPermanentLocked().equals("해제하기")){
            throw new LockedException("계정이 정지되었습니다. 관리자에게 문의하세요.");
        }


        if (request.getSession().getAttribute("data") == null){
            log.info("쿠키 만료");

            Cookie userEmail = new Cookie("userEmail", null);
            Cookie userPassword = new Cookie("userPassword", null);

            userEmail.setMaxAge(0);
            userPassword.setMaxAge(0);
            response.addCookie(userEmail);
            response.addCookie(userPassword);

        } else if (request.getSession().getAttribute("data") != null){
            log.info("쿠키 생성");

            Cookie userEmail = new Cookie("userEmail", request.getParameter("email"));
            Cookie userPassword = new Cookie("userPassword", request.getParameter("password"));

            response.addCookie(userEmail);
            response.addCookie(userPassword);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        } else {
            redirectStrategy.sendRedirect(request, response, "/user");
        }

    }


}
