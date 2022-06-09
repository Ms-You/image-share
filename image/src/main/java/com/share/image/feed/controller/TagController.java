package com.share.image.feed.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
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
@RequestMapping("/user")
public class TagController {
    private final TagRepository tagRepository;

    // 로그인 후 태그 목록 보여줌
    @GetMapping
    public String mainPage(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "main";
    }


    @GetMapping("/tag/{tag_id}")
    public String tags(@PathVariable(name = "tag_id") Long tagId, Model model){
        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });

        List<Feed> feeds = tag.getFeeds();
        model.addAttribute("feeds", feeds);

        return "tag/feeds";
    }


}
