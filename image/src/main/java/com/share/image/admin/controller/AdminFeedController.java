package com.share.image.admin.controller;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminFeedController {

    private final FeedRepository feedRepository;

    // 특정 피드 보기
    @GetMapping("/feed/{feedId}")
    public String feedView(@PathVariable(name = "feedId") Long feedId, Model model){

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        model.addAttribute("feed", feed);

        return "admin/feed/feedView";
    }


    // 피드 삭제
    @ResponseBody
    @DeleteMapping("/feed/{feedId}")
    public ResponseEntity deleteFeed(@PathVariable(name = "feedId") Long feedId){

        try{
            Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
                return new IllegalArgumentException("존재하지 않는 피드입니다.");
            });

            feedRepository.deleteById(feedId);

            return new ResponseEntity(feed.getTag().getId(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 피드 검색
    @GetMapping("/feed/search")
    public String searchFeed(
            String keyword, Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

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
