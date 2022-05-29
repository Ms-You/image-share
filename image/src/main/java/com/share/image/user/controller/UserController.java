package com.share.image.user.controller;

import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원가입 페이지 이동
    @GetMapping("/user/join")
    public String join(){
        return "/user/join";
    }

    // 회원가입
    @PostMapping("/user/join")
    public String join(JoinRequestDto joinRequestDto){
        User joinedUser = userService.signUp(joinRequestDto);
        return "redirect:/";
    }

    // 로그인 페이지 이동
    @GetMapping("/user/login")
    public String login(){
        return "/user/login";
    }


}
