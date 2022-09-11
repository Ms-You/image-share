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

    @PostMapping("/likes/feed/{feedId}/reply/{replyId}")
    public String likesReply(@PathVariable(name = "feedId") Long feedId,
                            @PathVariable(name = "replyId") Long replyId,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyLikeService.likeReply(replyId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unLikes/feed/{feedId}/reply/{replyId}")
    public String unLikesReply(@PathVariable(name = "feedId") Long feedId,
                              @PathVariable(name = "replyId") Long replyId,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyLikeService.unLikeReply(replyId, principalDetails.getUser().getId());

        return "redirect:/user/feed/" + feedId;
    }


}
