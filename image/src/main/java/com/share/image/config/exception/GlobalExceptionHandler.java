package com.share.image.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private String VIEW_PATH = "/error/";


    @ExceptionHandler(value = {MissingServletRequestParameterException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class})
    public String error_400(Model model){
        model.addAttribute("code", ErrorCode.BAD_PARAMETER.getHttpStatus());
        model.addAttribute("msg", ErrorCode.BAD_PARAMETER.getMessage());

        return VIEW_PATH + "400";
    }


    @ExceptionHandler(AuthenticationException.class)
    public String error_401(Model model){
        model.addAttribute("code", ErrorCode.UNAUTHORIZED_MEMBER.getHttpStatus());
        model.addAttribute("msg", ErrorCode.UNAUTHORIZED_MEMBER.getMessage());

        return VIEW_PATH + "401";
    }


    @ExceptionHandler(value = {NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public String error_404(Model model){
        model.addAttribute("code", ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus());
        model.addAttribute("msg", ErrorCode.RESOURCE_NOT_FOUND.getMessage());

        return VIEW_PATH + "404";
    }


    @ExceptionHandler(value = {IllegalArgumentException.class, UsernameNotFoundException.class})
    public String handlePathVariableException(Model model){
        model.addAttribute("code", ErrorCode.RESOURCE_ERROR.getHttpStatus());
        model.addAttribute("msg", ErrorCode.RESOURCE_ERROR.getMessage());

        return VIEW_PATH + "500";
    }


    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    public String handleSQLException(Model model){
        model.addAttribute("code", ErrorCode.SQL_ERROR.getHttpStatus());
        model.addAttribute("msg", ErrorCode.SQL_ERROR.getMessage());

        return VIEW_PATH + "500";
    }


    @ExceptionHandler(Exception.class)
    public String handleAllException(Model model){
        model.addAttribute("code", ErrorCode.SERVER_ERROR.getHttpStatus());
        model.addAttribute("msg", ErrorCode.SERVER_ERROR.getMessage());

        return VIEW_PATH + "500";
    }


    @ExceptionHandler(GlobalException.class)
    public String handleGlobalException(GlobalException ex, Model model){
        model.addAttribute("code", ex.getErrorCode().getHttpStatus());
        model.addAttribute("msg", ex.getErrorCode().getMessage());

        return VIEW_PATH + ex.getErrorCode().getHttpStatus().value();
    }


}
