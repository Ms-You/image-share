package com.share.image.user.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.dto.UpdateRequestDto;
import com.share.image.user.dto.UsersDtoValidator;
import com.share.image.user.repository.UserRepository;
import com.share.image.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UsersDtoValidator usersDtoValidator;


    @PostMapping("/auth/join")
    public String join(@Valid JoinRequestDto joinRequestDto, Errors errors, Model model){
        usersDtoValidator.validate(joinRequestDto, errors);

        if (errors.hasErrors()){
            model.addAttribute("joinRequestDto", joinRequestDto);

            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "user/join";
        }
        userService.signUp(joinRequestDto);
        return "/user/login";
    }



    // 회원 정보 페이지로 이동
    @GetMapping("/user/profile")
    public String uploadProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        model.addAttribute("user", user);

        return "/user/profile";
    }


    // 회원 정보 수정 페이지로 이동
    @GetMapping("/user/update")
    public String update(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        model.addAttribute("user", user);

        return "user/update";
    }


    // 회원 정보 수정
    @PostMapping("/user/update")
    public String uploadProfile(
            @Valid UpdateRequestDto updateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam MultipartFile file) throws IOException {
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        userService.updateProfile(user, updateRequestDto, file);

        //세션 정보 변경
        principalDetails.updateUser(user);

        return "redirect:/user/profile";

    }



}
