package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepository userRepository;


    // 사용자 리스트 보기
    @GetMapping("/users")
    public String userList(Model model){

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "admin/user/userList";
    }


    // 특정 사용자 보기
    @GetMapping("/user/{user_id}")
    public String userView(@PathVariable(name = "user_id") Long userId, Model model){

        User user = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");

        });

        List<Feed> feeds = user.getFeeds();

        model.addAttribute("user", user);
        model.addAttribute("feeds", feeds);

        return "admin/user/userView";
    }


}
