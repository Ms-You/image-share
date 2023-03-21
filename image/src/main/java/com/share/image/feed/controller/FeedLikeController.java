package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.FeedLike;
import com.share.image.feed.repository.FeedLikeRepository;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
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
    private final FeedLikeRepository feedLikeRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;


    @PostMapping("/likes/feed/{feedId}")
    public String likesFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        feedLikeRepository.save(FeedLike.create(user, feed));

        return "redirect:/user/feed/" + feed.getId();
    }


    @PostMapping("/unLikes/feed/{feedId}")
    public String unLikesFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        FeedLike feedLike = feedLikeRepository.findByUserAndFeed(user, feed).orElseThrow(
                () -> new GlobalException(ErrorCode.FEED_LIKE_ERROR)
        );

        feedLikeRepository.delete(feedLike);

        return "redirect:/user/feed/" + feed.getId();
    }



}
