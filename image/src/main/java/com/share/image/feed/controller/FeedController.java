package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.dto.FeedDtoValidator;
import com.share.image.feed.dto.FeedRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.repository.TagRepository;
import com.share.image.feed.service.FeedService;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class FeedController {

    private final FeedDtoValidator feedDtoValidator;
    private final FeedService feedService;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

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

        feedService.createFeed(user, feedRequestDto, file);

        return "redirect:/user";

    }


    @GetMapping("/feed/{feed_id}")
    public String tags(@PathVariable(name = "feed_id") Long feedId, Model model){
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        model.addAttribute("feed", feed);

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
    @PostMapping("/feed/update/{feed_id}")
    public String updateFeed(@AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feed_id") Long feedId,
            @Valid FeedRequestDto feedRequestDto,
            Errors errors,
            Model model,
            @RequestParam MultipartFile file,
            @RequestParam String tagName) throws UnsupportedEncodingException {

        User user = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
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



}
