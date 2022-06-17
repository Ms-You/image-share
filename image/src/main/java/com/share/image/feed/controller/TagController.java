package com.share.image.feed.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class TagController {
    private final TagRepository tagRepository;
    private final FeedRepository feedRepository;

    // 로그인 후 태그 목록 보여줌
    @GetMapping
    public String mainPage(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "main";
    }


    @GetMapping("/tag")
    public String tags(@RequestParam(name = "tag_id") Long tagId, Model model, @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Tag tag = tagRepository.findById(tagId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 태그입니다.");
        });

        Page<Feed> feeds = feedRepository.findByTag(tag, pageable);

        model.addAttribute("tag", tag);
        model.addAttribute("feeds", feeds);

        return "tag/feeds";
    }


}
