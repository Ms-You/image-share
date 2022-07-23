package com.share.image.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private String rememberChk;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        rememberChk = request.getParameter("rememberChk");
        request.getSession().setAttribute("data", rememberChk);

    }


}
