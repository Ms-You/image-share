package com.share.image.user.controller;

import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

//     회원가입
    @ResponseBody
    @PostMapping("/user/join")
    public ResponseEntity<?> join(@RequestBody JoinRequestDto joinRequestDto) {
        try{
            User joinedUser = userService.signUp(joinRequestDto);
            return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("회원가입에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }



}
