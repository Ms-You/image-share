package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminFeedController {

    private final FeedRepository feedRepository;

    // 특정 피드 보기
    @GetMapping("/feed/{feed_id}")
    public String feedView(@PathVariable(name = "feed_id") Long feedId, Model model){
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        model.addAttribute("feed", feed);

        return "admin/feed/feedView";
    }


    // 피드 삭제
    @GetMapping("/tag/delete/feed/{feed_id}")
    public String deleteFeed(@PathVariable("feed_id") Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        feedRepository.deleteById(feedId);
        Long tagId = feed.getTag().getId();

        return "redirect:/admin/tag/"+ tagId;
    }

}
