package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
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

        return "redirect:/admin/tag/?tag_id="+ tagId;
    }

    // 피드 삭제 (수정)
    @GetMapping("/feed/delete/feed/{feed_id}")
    public String deleteSpecificFeed(@PathVariable("feed_id") Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        feedRepository.deleteById(feedId);
        Long tagId = feed.getTag().getId();

        return "redirect:/admin/tag/list";
    }


    // 피드 검색
    @GetMapping("/feed/search")
    public String searchFeed(String keyword, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Feed> feedList = feedRepository.findByTitleContaining(keyword, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feedList.getTotalPages() ? feedList.getTotalPages() : tempEndPage;

        model.addAttribute("feedList", feedList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/feed/searchPage";
    }


}
