package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.service.ReplyLikeService;
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

    private final ReplyLikeService replyLikeService;

    @PostMapping("/like/feed/{feed_id}/reply/{reply_id}")
    public String likeReply(@PathVariable(name = "feed_id") Long feedId,
                            @PathVariable(name = "reply_id") Long replyId,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyLikeService.likeReply(replyId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unLike/feed/{feed_id}/reply/{reply_id}")
    public String unLikeReply(@PathVariable(name = "feed_id") Long feedId,
                              @PathVariable(name = "reply_id") Long replyId,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyLikeService.unLikeReply(replyId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }


}
