package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminFeedController {

    private final TagRepository tagRepository;
    private final FeedRepository feedRepository;

    @GetMapping("/tag/list")
    public String tagList(Model model){
        List<Tag> tagList = tagRepository.findAll();
        model.addAttribute("tagList", tagList);

        return "admin/feed/tagList";
    }


    // 피드 모음 페이지로 이동
    @GetMapping("/tag/{tag_id}")
    public String feeds(@PathVariable(name = "tag_id") Long tagId, Model model){

        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });

        List<Feed> feeds = tag.getFeeds();
        model.addAttribute("feeds", feeds);

        return "admin/feed/feeds";
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
