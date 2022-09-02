package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Subscribe;
import com.share.image.feed.service.SubscribeService;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class SubscribeController {

    private final SubscribeService subscribeService;
    private final UserRepository userRepository;

    @PostMapping("/subscribe/{to_user_id}/{feed_id}")
    public String subscribeUser(@PathVariable(name = "to_user_id") Long toUserId,
                                @PathVariable(name = "feed_id") Long feedId,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.subscribe(user.getId(), toUserId);

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unSubscribe/{to_user_id}/{feed_id}")
    public String unSubscribeUser(@PathVariable(name = "to_user_id") Long toUserId,
                                  @PathVariable(name = "feed_id") Long feedId,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.unSubscribe(user.getId(), toUserId);

        return "redirect:/user/feed/" + feedId;
    }

    @GetMapping("/subscribe")
    public String subscribes(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,
                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Page<Subscribe> subscribes = subscribeService.findSubscribeList(user, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > subscribes.getTotalPages() ? subscribes.getTotalPages() : tempEndPage;

        model.addAttribute("subscribes", subscribes);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "user/subscribes";
    }

    // 구독 유저 보기
    @GetMapping("/subscribe/{user_id}")
    public String userView(@PathVariable(name = "user_id") Long userId, Model model){

        User user = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");

        });

        model.addAttribute("user", user);

        return "user/subscribeUser";
    }

}
