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

    @PostMapping("/like/feed/{feed_id}")
    public String likeFeed(@PathVariable(name = "feed_id") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.likeFeed(feedId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unLike/feed/{feed_id}")
    public String unLikeFeed(@PathVariable(name = "feed_id") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.unLikeFeed(feedId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }



}
