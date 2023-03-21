package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.dto.ReplyRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.feed.service.ReplyLikeService;
import com.share.image.feed.service.ReplyService;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class ReplyController {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReplyService replyService;
    private final ReplyRepository replyRepository;
    private final ReplyLikeService replyLikeService;

    // 댓글 작성
    @PostMapping("/feed/{feedId}/reply")
    public String writeReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @Valid ReplyRequestDto replyRequestDto,
            Errors errors,
            Model model) {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_ERROR)
        );

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        List<Reply> replies = replyRepository.findByFeed(feed);
        // 댓글별 좋아요 변경
        for(Reply reply: replies){
            if (replyLikeService.isUserLikeReply(user, reply)) reply.updateReplyLikeStatus("/img/full_heart.png");
            else reply.updateReplyLikeStatus("/img/empty_heart.png");
        }

        if (errors.hasErrors()) {
            model.addAttribute("replyRequestDto", replyRequestDto);
            model.addAttribute("replies", replies);
            model.addAttribute("feed", feed);

            Map<String, String> validatorResult = replyService.validateHandling(errors);
            for (String key: validatorResult.keySet())
                model.addAttribute(key, validatorResult.get(key));

            return "feed/view :: #replyList";
        }

        Reply reply = replyService.createReply(user, replyRequestDto, feed);

        replies.add(reply);
        model.addAttribute("replies", replies);
        model.addAttribute("feed", feed);

        return "feed/view :: #replyList";

    }


    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("/feed/reply/{replyId}")
    public ResponseEntity deleteReply(@PathVariable(name = "replyId") Long replyId){

        try{
            Reply reply = replyRepository.findById(replyId).orElseThrow(
                    ()-> new GlobalException(ErrorCode.REPLY_ERROR)
            );

            replyRepository.delete(reply);

            return new ResponseEntity(reply.getFeed().getId(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
