package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public String userList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        Page<User> users = userRepository.findAll(pageable);
        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > users.getTotalPages() ? users.getTotalPages() : tempEndPage;

        model.addAttribute("users", users);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
