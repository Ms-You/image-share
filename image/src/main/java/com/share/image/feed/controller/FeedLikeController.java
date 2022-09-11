package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/likes/feed/{feedId}")
    public String likesFeed(@PathVariable(name = "feedId") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.likeFeed(feedId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unLikes/feed/{feedId}")
    public String unLikesFeed(@PathVariable(name = "feedId") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.unLikeFeed(feedId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }



}
