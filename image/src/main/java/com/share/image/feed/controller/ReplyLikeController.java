package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.ReplyLike;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReplyLikeRepository;
import com.share.image.feed.repository.ReplyRepository;
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
public class ReplyLikeController {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;


    @PostMapping("/likes/feed/{feedId}/reply/{replyId}")
    public String likesReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @PathVariable(name = "replyId") Long replyId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        Reply reply = replyRepository.findById(replyId).orElseThrow(
                ()-> new GlobalException(ErrorCode.REPLY_ERROR)
        );

        replyLikeRepository.save(ReplyLike.create(user, reply));

        return "redirect:/user/feed/" + feed.getId();
    }


    @PostMapping("/unLikes/feed/{feedId}/reply/{replyId}")
    public String unLikesReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @PathVariable(name = "replyId") Long replyId) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        Reply reply = replyRepository.findById(replyId).orElseThrow(
                ()-> new GlobalException(ErrorCode.REPLY_ERROR)
        );

        ReplyLike replyLike = replyLikeRepository.findByUserAndReply(user, reply).orElseThrow(
                () -> new GlobalException(ErrorCode.REPLY_LIKE_ERROR)
        );

        replyLikeRepository.delete(replyLike);

        return "redirect:/user/feed/" + feed.getId();
    }


}
