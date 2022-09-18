package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.feed.service.ReplyLikeService;
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
public class ReplyLikeController {

    private final ReplyLikeService replyLikeService;
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;


    @PostMapping("/likes/feed/{feedId}/reply/{replyId}")
    public String likesReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @PathVariable(name = "replyId") Long replyId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("피드가 존재하지 않습니다.");
        });

        Reply reply = replyRepository.findById(replyId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 댓글입니다.");
        });

        replyLikeService.likeReply(user, reply);

        return "redirect:/user/feed/" + feed.getId();
    }


    @PostMapping("/unLikes/feed/{feedId}/reply/{replyId}")
    public String unLikesReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @PathVariable(name = "replyId") Long replyId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("피드가 존재하지 않습니다.");
        });

        Reply reply = replyRepository.findById(replyId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 댓글입니다.");
        });

        replyLikeService.unLikeReply(user, reply);

        return "redirect:/user/feed/" + feed.getId();
    }


}
