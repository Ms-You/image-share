package com.share.image.feed.controller;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @GetMapping("/tag/{tagId}")
    public String feedsOfTag(
            @PathVariable(name = "tagId") Long tagId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

        Tag tag = tagRepository.findById(tagId).orElseThrow(
                ()-> new GlobalException(ErrorCode.TAG_ERROR)
        );

        Page<Feed> feeds = feedRepository.findByTag(tag, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feeds.getTotalPages() ? feeds.getTotalPages() : tempEndPage;

        model.addAttribute("tag", tag);
        model.addAttribute("feeds", feeds);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "tag/feeds";
    }


}
