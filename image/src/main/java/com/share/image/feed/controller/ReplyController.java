package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.dto.ReplyRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.feed.service.ReplyService;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class ReplyController {

    private final ReplyService replyService;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;

    // 댓글 작성
    @PostMapping("/reply/feed/{feed_id}")
    public String createReply(@PathVariable(name = "feed_id") Long feedId,
                              @AuthenticationPrincipal PrincipalDetails principalDetails,
                              @Valid ReplyRequestDto replyRequestDto,
                              Errors errors,
                              Model model) {
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        if (errors.hasErrors()) {
            List<Reply> replies = replyRepository.findByFeed(feed);
            model.addAttribute("replyRequestDto", replyRequestDto);
            model.addAttribute("replies", replies);
            model.addAttribute("feed", feed);

            Map<String, String> validatorResult = replyService.validateHandling(errors);
            for (String key: validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "feed/view :: #replyList";
        }

        replyService.createReply(user, replyRequestDto, feed);

        List<Reply> replies = replyRepository.findByFeed(feed);
        model.addAttribute("replies", replies);
        model.addAttribute("feed", feed);

        return "feed/view :: #replyList";

    }


    // 댓글 삭제
    @GetMapping("/feed/delete/reply/{reply_id}")
    public String deleteFeed(@PathVariable("reply_id") Long replyId) {
        Reply reply = replyService.findByReplyId(replyId);
        // 댓글 삭제
        replyService.deleteReply(reply);

        Long feedId = reply.getFeed().getId();

        return "redirect:/user/feed/" + feedId;
    }



}
