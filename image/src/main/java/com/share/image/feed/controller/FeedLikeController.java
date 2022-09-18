package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.service.FeedLikeService;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class FeedLikeController {

    private final FeedLikeService likeService;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;


    @PostMapping("/likes/feed/{feedId}")
    public String likesFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        likeService.likeFeed(user, feed);

        return "redirect:/user/feed/" + feed.getId();
    }


    @PostMapping("/unLikes/feed/{feedId}")
    public String unLikesFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        likeService.unLikeFeed(user, feed);

        return "redirect:/user/feed/" + feed.getId();
    }



}
