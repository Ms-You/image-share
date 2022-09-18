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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ReplyRepository replyRepository;
    private final ViewService viewService;
    private final FeedLikeService feedLikeService;
    private final ReplyLikeService replyLikeService;
    private final SubscribeService subscribeService;
    private final FeedRepository feedRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;


    // 피드 생성 페이지로 이동
    @GetMapping("/feed")
    public String createFeed(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "/feed/create";
    }


    // 피드 생성
    @PostMapping("/feed")
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

        Tag tag = tagRepository.findByName(tagName).orElseThrow(()->{
            return new IllegalArgumentException("일치하는 태그명을 찾을 수 없습니다.");
        });

        // 파일 유효성 검사를 위해 파일명을 dto 에 넣어줌
        feedRequestDto.insertImage(file.getOriginalFilename());
        feedDtoValidator.validate(feedRequestDto, errors);

        // 유효성 검사
        if (errors.hasErrors()){
            List<Tag> tags = tagRepository.findAll();
            model.addAttribute("feedRequestDto", feedRequestDto);
            model.addAttribute("tags", tags);

            Map<String, String> validatorResult = feedService.validateHandling(errors);
            for (String key : validatorResult.keySet())
                model.addAttribute(key, validatorResult.get(key));

            return "feed/create";
        }

        Feed feed = feedService.createFeed(user, feedRequestDto, tag, file);

        // 피드 작성자는 피드 봤다고 침
        viewService.viewFeed(feed.getId(), user.getId());

        return "redirect:/user";

    }


    // 특정 피드 보기
    @GetMapping("/feed/{feedId}")
    public String feedView(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        List<Reply> replies = replyRepository.findByFeed(feed);

        // 구독 상태 변경
        if (subscribeService.isUserSubscribe(feed.getWriter(), user))
            model.addAttribute("subscribeStatus", "/img/do_sub.png");
        else
            model.addAttribute("subscribeStatus", "/img/un_sub.png");

        // 피드 좋아요 변경
        if (feedLikeService.isUserLikeFeed(user, feed))
            model.addAttribute("feedLikeStatus", "/img/full_heart.png");
        else
            model.addAttribute("feedLikeStatus", "/img/empty_heart.png");

        // 댓글별 좋아요 변경
        for(Reply reply: replies){
            if (replyLikeService.isUserLikeReply(user, reply))
                reply.updateReplyLikeStatus("/img/full_heart.png");
            else
                reply.updateReplyLikeStatus("/img/empty_heart.png");
        }

        // 피드를 안 본 경우 조회수 1 증가
        if (viewService.isUserViewFeed(user, feed))
            viewService.viewFeed(feed.getId(), user.getId());

        model.addAttribute("prevFeed", feedRepository.leadFeedId(feed.getId(), feed.getTag().getId()));
        model.addAttribute("nextFeed", feedRepository.lagFeedId(feed.getId(), feed.getTag().getId()));
        model.addAttribute("feed", feed);
        model.addAttribute("replies", replies);

        return "feed/view";
    }


    // 구독한 사용자의 피드 보기 (prevFeed, nextFeed 때문에 생성)
    @GetMapping("/subscription/feed/{feedId}")
    public String subscribedFeedView(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        List<Reply> replies = replyRepository.findByFeed(feed);

        // 구독 상태 변경
        if (subscribeService.isUserSubscribe(feed.getWriter(), user))
            model.addAttribute("subscribeStatus", "/img/do_sub.png");
        else
            model.addAttribute("subscribeStatus", "/img/un_sub.png");

        // 피드 좋아요 변경
        if (feedLikeService.isUserLikeFeed(user, feed))
            model.addAttribute("feedLikeStatus", "/img/full_heart.png");
        else
            model.addAttribute("feedLikeStatus", "/img/empty_heart.png");

        // 댓글별 좋아요 변경
        for(Reply reply: replies){
            if (replyLikeService.isUserLikeReply(user, reply))
                reply.updateReplyLikeStatus("/img/full_heart.png");
            else
                reply.updateReplyLikeStatus("/img/empty_heart.png");
        }

        // 피드를 안 본 경우 조회수 1 증가
        if (viewService.isUserViewFeed(user, feed))
            viewService.viewFeed(feed.getId(), user.getId());

        model.addAttribute("prevFeed", feedRepository.toUsersLeadFeedId(feed.getId(), feed.getWriter().getId()));
        model.addAttribute("nextFeed", feedRepository.toUsersLagFeedId(feed.getId(), feed.getWriter().getId()));
        model.addAttribute("feed", feed);
        model.addAttribute("replies", replies);

        return "feed/subscribedFeedView";
    }


    // 피드 수정페이지 이동
    @GetMapping("/modifying/feed/{feedId}")
    public String updateFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            Model model){

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        if (user.getId() != feed.getWriter().getId())
            return "feed/exception";

        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("feed", feed);

        return "feed/update";
    }


    // 피드 수정
    @PutMapping("/feed/{feedId}")
    public String updateFeed(
            @PathVariable(name = "feedId") Long feedId,
            @Valid FeedRequestDto feedRequestDto,
            Errors errors,
            Model model,
            @RequestParam MultipartFile file,
            @RequestParam String tagName) throws UnsupportedEncodingException {

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        Tag tag = tagRepository.findByName(tagName).orElseThrow(()->{
            return new IllegalArgumentException("일치하는 태그명을 찾을 수 없습니다.");
        });

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
            for (String key : validatorResult.keySet())
                model.addAttribute(key, validatorResult.get(key));

            return "feed/update";
        }

        feedService.updateFeed(feed.getWriter(), feed, feedRequestDto, tag, file);

        return "redirect:/user";

    }


    // 피드 삭제
    @ResponseBody
    @DeleteMapping("/feed/{feedId}")
    public ResponseEntity deleteFeed(@PathVariable(name = "feedId") Long feedId){

        try {
            Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
                return new IllegalArgumentException("존재하지 않는 피드입니다.");
            });

            feedRepository.deleteById(feedId);

            return new ResponseEntity(feed.getTag().getId(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // 현재 사용자의 피드 관리
    @GetMapping("/feeds")
    public String currentUsersFeeds(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

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


    // 구독한 사용자의 피드 보기
    @GetMapping("/toUser/{userId}/feeds")
    public String subscriptionFeedsView(
            @PathVariable(name = "userId") Long userId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

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

        return "/user/subscribedFeeds";
    }


    // 피드 검색
    @GetMapping("/feed/search")
    public String searchFeed(
            String keyword, Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Feed> searchList = feedRepository.findByTitleContaining(keyword, pageable);

        int startPage = (int) (Math.floor(pageable.getPageNumber() / pageable.getPageSize()) * pageable.getPageSize() + 1);
        int tempEndPage = startPage + pageable.getPageSize() - 1;
        int endPage = tempEndPage > searchList.getTotalPages() ? searchList.getTotalPages() : tempEndPage;

        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "feed/searchPage";
    }


    // 좋아요 표시한 피드 목록
    @GetMapping("/likes/feeds")
    public String feedsLikes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model){

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
    public String searchFeedByType(
            @RequestParam String searchType, Model model,
            @PageableDefault(size = 5) Pageable pageable) {

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
