package com.share.image.admin.controller;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReportRepository;
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

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final FeedRepository feedRepository;

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
    @GetMapping("/user/{userId}")
    public String userView(@PathVariable(name = "userId") Long userId, Model model){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        model.addAttribute("user", user);
        model.addAttribute("reportCount", reportRepository.findByToUser(user).size());

        return "admin/user/userView";
    }


    // 사용자 검색
    @GetMapping("/user/search")
    public String searchUser(
            String keyword, Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<User> userList = userRepository.findByNickNameContaining(keyword, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > userList.getTotalPages() ? userList.getTotalPages() : tempEndPage;

        model.addAttribute("userList", userList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/user/searchPage";
    }


    // 일시 정지된 사용자 관리
    @GetMapping("/temporary/users")
    public String temporaryStoppedUser(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model){

        Page<User> users = userRepository.findAllByTemporaryLocked(pageable, "해제하기");

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > users.getTotalPages() ? users.getTotalPages() : tempEndPage;

        model.addAttribute("users", users);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/user/temporary";
    }


    // 영구 정지된 사용자 관리
    @GetMapping("/permanent/users")
    public String permanentStoppedUser(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model){

        Page<User> users = userRepository.findAllByPermanentLocked(pageable, "해제하기");

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > users.getTotalPages() ? users.getTotalPages() : tempEndPage;

        model.addAttribute("users", users);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/user/permanent";
    }


    // 회원 관리에서의 특정 사용자가 생성한 피드 목록
    @GetMapping("/user/{userId}/feeds")
    public String feedsView(
            @PathVariable(name = "userId") Long userId, Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Page<Feed> feeds = feedRepository.findByWriter(user, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feeds.getTotalPages() ? feeds.getTotalPages() : tempEndPage;

        model.addAttribute("user", user);
        model.addAttribute("feeds", feeds);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/admin/user/feedsView";
    }


}
