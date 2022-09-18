package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.ReportType;
import com.share.image.feed.dto.ReportRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.service.ReportService;
import com.share.image.user.domain.User;
import com.share.image.feed.dto.ReportDtoValidator;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user/report")
public class ReportController {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReportService reportService;
    private final ReportDtoValidator reportDtoValidator;

    @ModelAttribute("reportTypes")
    public ReportType[] reportTypes() {
        return ReportType.values();
    }

    // 특정 피드 신고 페이지로 이동
    @GetMapping("/feed/{feedId}")
    public String reportingPage(@PathVariable(name = "feedId") Long feedId, Model model){

        model.addAttribute("feedId", feedId);
        model.addAttribute("reportRequestDto", new ReportRequestDto());

        return "feed/report";
    }


    // 특정 피드 신고
    @PostMapping("/feed/{feedId}")
    public String reportFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "feedId") Long feedId,
            @Valid @ModelAttribute ReportRequestDto reportRequestDto,
            BindingResult bindingResult, Model model) {

        User fromUser = userRepository.findById(principalDetails.getUser().getId()).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        reportDtoValidator.validate(reportRequestDto, bindingResult);

        if (bindingResult.hasErrors()){
        // 유효성 검사
            model.addAttribute("feedId", feedId);
            model.addAttribute("reportRequestDto", reportRequestDto);

            Map<String, String> validatorResult = reportService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet())
                model.addAttribute(key, validatorResult.get(key));

            return "feed/report";
        }

        reportService.reportingFeed(fromUser, feed, reportRequestDto);
        model.addAttribute("message", "신고가 접수되었습니다.");
        model.addAttribute("feedId", feedId);

        return "feed/report";
    }


}
