package com.share.image.user.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.Profile;
import com.share.image.user.domain.User;
import com.share.image.user.repository.ProfileRepository;
import com.share.image.user.repository.UserRepository;
import com.share.image.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;

    @GetMapping("/app/profile")
    public String uploadProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Profile profile = profileRepository.findByUserId(user.getId());
        model.addAttribute("profile", profile);

        return "/user/profile";
    }

    @PostMapping("/app/profile")
    public String uploadProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam String nickName,
            @RequestParam String intro,
            @RequestParam MultipartFile file,
            Model model) throws IOException{
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        profileService.updateProfile(user, nickName, intro, file);
        Profile profile = profileRepository.findByUserId(user.getId());
        model.addAttribute("profile", profile);

        return "/user/profile";

    }



}
