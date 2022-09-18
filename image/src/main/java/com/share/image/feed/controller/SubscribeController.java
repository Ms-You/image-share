package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Subscribe;
import com.share.image.feed.repository.FeedRepository;
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
    private final FeedRepository feedRepository;


    @PostMapping("/subscribe/toUser/{toUserId}/feed/{feedId}")
    public String subscribeUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        subscribeService.subscribe(user.getId(), toUser.getId());

        return "redirect:/user/feed/" + feed.getId();
    }


    @PostMapping("/unSubscribe/toUser/{toUserId}/feed/{feedId}")
    public String unSubscribeUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        subscribeService.unSubscribe(user.getId(), toUser.getId());

        return "redirect:/user/feed/" + feed.getId();
    }


    // 구독 관리 페이지
    @GetMapping("/subscribe")
    public String subscribes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

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
    @GetMapping("/toUser/{userId}")
    public String subUserView(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "userId") Long userId, Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        // 구독 상태 변경
        if (subscribeService.isUserSubscribe(toUser, user))
            model.addAttribute("subscribeStatus", "/img/do_sub.png");
        else
            model.addAttribute("subscribeStatus", "/img/un_sub.png");

        model.addAttribute("user", toUser);

        return "user/subscribeUser";
    }


    @PostMapping("/subscribe/toUser/{toUserId}")
    public String userSubscribe(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.subscribe(user.getId(), toUser.getId());

        return "redirect:/user/toUser/" + toUser.getId();
    }


    @PostMapping("/unSubscribe/toUser/{toUserId}")
    public String userUnSubscribe(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.unSubscribe(user.getId(), toUser.getId());

        return "redirect:/user/toUser/" + toUser.getId();
    }


    @PostMapping("/sub/toUser/{toUserId}")
    public String userSub(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.subscribe(user.getId(), toUser.getId());

        return "redirect:/user/" + toUser.getId();
    }


    @PostMapping("/unSub/toUser/{toUserId}")
    public String userUnSub(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "toUserId") Long toUserId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        User toUser = userRepository.findById(toUserId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        subscribeService.unSubscribe(user.getId(), toUser.getId());

        return "redirect:/user/" + toUser.getId();
    }

}
