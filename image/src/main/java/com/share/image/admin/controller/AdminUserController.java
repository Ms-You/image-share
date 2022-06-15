package com.share.image.admin.controller;

import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepository userRepository;


    @GetMapping("/users")
    public String userList(Model model){

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "admin/user/userList";
    }

}
