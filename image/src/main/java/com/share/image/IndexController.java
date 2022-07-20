package com.share.image;

import com.share.image.user.dto.JoinRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    // 회원가입 페이지 이동
    @GetMapping("/auth/join")
    public String join(JoinRequestDto joinRequestDto){
        return "/user/join";
    }

    // 로그인 페이지 이동
    @GetMapping("/auth/login")
    public String login(@RequestParam(value = "error", required = false)String error,
                        @RequestParam(value = "exception", required = false)String exception,
                        Model model){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/user/login";
    }

    // 로그인 실패 시 이동
    @PostMapping("/auth/login")
    public String fail(){
        return "user/login";
    }

}
