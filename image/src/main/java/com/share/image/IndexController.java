package com.share.image;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    // 회원가입 페이지 이동
    @GetMapping("/user/join")
    public String join(){

        return "/user/join";
    }

    // 로그인 페이지 이동
    @GetMapping("/user/login")
    public String login(){

        return "/user/login";
    }

}
