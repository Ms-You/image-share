package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Reply;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.dto.FeedDtoValidator;
import com.share.image.feed.dto.FeedRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.ReplyRepository;
import com.share.image.feed.repository.TagRepository;
import com.share.image.feed.service.*;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class FeedController {

    private final FeedDtoValidator feedDtoValidator;
    private final FeedService feedService;
    private final ViewService viewService;
    private final FeedLikeService feedLikeService;
    private final ReplyLikeService replyLikeService;
    private final SubscribeService subscribeService;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;

    // 피드 생성 페이지로 이동
    @GetMapping("/new/feed")
    public String createFeed(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "/feed/create";
    }


    // 피드 생성
    @PostMapping("/new/feed")
    public String createFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid FeedRequestDto feedRequestDto,
            Errors errors,
            Model model,
            @RequestParam MultipartFile file,
            @RequestParam String tagName) throws UnsupportedEncodingException {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        // select-option 에서 태그 이름을 받아와서 dto 에 넣어줌
        Tag tag = tagRepository.findByName(tagName);
        feedRequestDto.insertTag(tag);

        // 파일 유효성 검사를 위해 파일명을 dto 에 넣어줌
        feedRequestDto.insertImage(file.getOriginalFilename());
        feedDtoValidator.validate(feedRequestDto, errors);

        // 유효성 검사
        if (errors.hasErrors()){
            List<Tag> tags = tagRepository.findAll();
            model.addAttribute("feedRequestDto", feedRequestDto);
            model.addAttribute("tags", tags);

            Map<String, String> validatorResult = feedService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "feed/create";
        }

        Feed createdFeed = feedService.createFeed(user, feedRequestDto, file);
        // 피드 작성자는 피드 봤다고 침
        viewService.viewFeed(createdFeed.getId(), user.getId());

        return "redirect:/user";

    }


    @GetMapping("/feed/{feed_id}")
    public String tags(@AuthenticationPrincipal PrincipalDetails principalDetails,
                        @PathVariable(name = "feed_id") Long feedId, Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        List<Reply> replies = replyRepository.findByFeed(feed);

        // 구독 상태 변경
        if (subscribeService.isUserSubscribe(feedId, user.getId()))
            model.addAttribute("subscribeStatus", "/img/do_sub.png");
        else
            model.addAttribute("subscribeStatus", "/img/un_sub.png");

        // 피드 좋아요 변경
        if (feedLikeService.isUserLikeFeed(feedId, user.getId()))
            model.addAttribute("feedLikeStatus", "/img/full_heart.png");
        else
            model.addAttribute("feedLikeStatus", "/img/empty_heart.png");


        // 댓글별 좋아요 변경
        for(Reply reply: replies){
            if (replyLikeService.isUserLikeReply(reply.getId(), user.getId()))
                reply.setReplyLikeStatus("/img/full_heart.png");
            else
                reply.setReplyLikeStatus("/img/empty_heart.png");
        }

        // 피드를 안 본 경우 조회수 1 증가
        if (viewService.isUserViewFeed(feedId, user.getId())) {
            viewService.viewFeed(feedId, user.getId());
        }

        model.addAttribute("prevFeed", feedRepository.leadFeedId(feed.getId(), feed.getTag().getId()));
        model.addAttribute("nextFeed", feedRepository.lagFeedId(feed.getId(), feed.getTag().getId()));
        model.addAttribute("feed", feed);
        model.addAttribute("replies", replies);

        return "feed/view";
    }

    // 피드 수정페이지 이동
    @GetMapping("/feed/update/{feed_id}")
    public String updateFeed(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @PathVariable(name = "feed_id") Long feedId, Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        if (user.getId() != feed.getWriter().getId()){
            return "feed/exception";
        }

        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);
        model.addAttribute("feed", feed);

        return "feed/update";
    }

    // 피드 수정
    @PutMapping("/feed/update/{feed_id}")
    public String updateFeed(@PathVariable(name = "feed_id") Long feedId,
            @Valid FeedRequestDto feedRequestDto,
            Errors errors,
            Model model,
            @RequestParam MultipartFile file,
            @RequestParam String tagName) throws UnsupportedEncodingException {

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        User user = feed.getWriter();

        // select-option 에서 태그 이름을 받아와서 dto 에 넣어줌
        Tag tag = tagRepository.findByName(tagName);
        feedRequestDto.insertTag(tag);

        // 파일 유효성 검사를 위해 파일명을 dto 에 넣어줌
        feedRequestDto.insertImage(file.getOriginalFilename());
        feedDtoValidator.validate(feedRequestDto, errors);

        // 유효성 검사
        if (errors.hasErrors()){
            List<Tag> tags = tagRepository.findAll();
            model.addAttribute("feedRequestDto", feedRequestDto);
            model.addAttribute("tags", tags);
            model.addAttribute("feed", feed);

            Map<String, String> validatorResult = feedService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "feed/update";
        }
        feedService.updateFeed(user, feed, feedRequestDto, file);
        return "redirect:/user";

    }


    // 피드 삭제
    @GetMapping("/feed/delete/{feed_id}")
    public String deleteFeed(@PathVariable("feed_id") Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        Long tagId = feed.getTag().getId();

        feedRepository.deleteById(feedId);

        return "redirect:/user/tag/?tag_id=" + tagId;
    }


    // 현재 사용자의 피드 관리
    @GetMapping("/feeds")
    public String manageFeeds(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Page<Feed> feeds = feedRepository.findByWriter(user, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feeds.getTotalPages() ? feeds.getTotalPages() : tempEndPage;

        model.addAttribute("feeds", feeds);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/user/feeds";
    }

    // 특정 사용자의 피드 관리
    @GetMapping("/feeds/{user_id}")
    public String feedsView(@PathVariable(name = "user_id") Long userId, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){

        User user = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Page<Feed> feeds = feedRepository.findByWriter(user, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > feeds.getTotalPages() ? feeds.getTotalPages() : tempEndPage;

        model.addAttribute("user", user);
        model.addAttribute("feeds", feeds);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/user/feedsView";
    }


    // 피드 검색
    @GetMapping("/feed/search")
    public String searchFeed(String keyword, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<Feed> searchList = feedService.search(keyword, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > searchList.getTotalPages() ? searchList.getTotalPages() : tempEndPage;

        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "feed/searchPage";
    }

    @GetMapping("/feeds/likes")
    public String manageFeedsLikes(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        int offset = pageable.getPageNumber()*5;

        List<Long> findIds = feedRepository.findFeedIdByUserIdAndLikesDesc(user.getId(), offset);
        List<Feed> findFeeds = new ArrayList<>();

        for (Long id: findIds){
            findFeeds.add(feedRepository.findById(id).orElseGet(null));
        }

        // totalPages 를 알아오기 위해 사용
        List<Long> feedSize = feedRepository.findSizeOfFeedByUserId(user.getId());

        int totalPages = (int)(Math.ceil(feedSize.size() * 1.0 / 5));

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > totalPages ? totalPages : tempEndPage;

        model.addAttribute("feeds", findFeeds);
        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "user/likeFeeds";
    }

    // 서치 타입 별 피드 조회
    @GetMapping("/feed/searchType")
    public String searchFeedByRecently(@RequestParam String searchType, Model model, @PageableDefault(size = 5) Pageable pageable) {
        int offset = pageable.getPageNumber()*5;
        int totalFeedsCount = feedRepository.findAll().size(); // 총 게시물 수를 알아옴

        List<Long> findIds;
        List<Feed> findFeeds = new ArrayList<>();
        if (searchType.equals("최신순")) {
            findIds = feedRepository.findFeedIdByCreatedDateDesc(offset);
            model.addAttribute("searchType", "최신순");
        }
        else if (searchType.equals("조회수 순")) {
            findIds = feedRepository.findFeedIdByViewsDesc(offset);
            model.addAttribute("searchType", "조회수 순");
        }
        else {
            findIds = feedRepository.findFeedIdByLikesDesc(offset);
            model.addAttribute("searchType", "좋아요 순");
        }

        for (Long id: findIds){
            findFeeds.add(feedRepository.findById(id).orElseGet(null));
        }

        int totalPages = (int)(Math.ceil(totalFeedsCount * 1.0 / 5));

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > totalPages ? totalPages : tempEndPage;

        model.addAttribute("feeds", findFeeds);
        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "feed/searchTypeFeeds";
    }

}
