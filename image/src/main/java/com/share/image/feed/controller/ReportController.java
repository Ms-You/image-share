package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.ReportType;
import com.share.image.feed.dto.ReportRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.feed.service.ReportService;
import com.share.image.user.domain.User;
import com.share.image.user.dto.ReportDtoValidator;
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
@RequestMapping("/user")
public class ReportController {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final ReportService reportService;
    private final ReportDtoValidator reportDtoValidator;

    @ModelAttribute("reportTypes")
    public ReportType[] reportTypes() {
        return ReportType.values();
    }

    @GetMapping("/report/feed/{feed_id}")
    public String reportPage(@PathVariable(name = "feed_id") Long feedId, Model model){

        model.addAttribute("feedId", feedId);
        model.addAttribute("reportRequestDto", new ReportRequestDto());

        return "feed/report";
    }

    @PostMapping("/report/feed/{feed_id}")
    public String reportFeed(@PathVariable(name = "feed_id") Long feedId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             @Valid @ModelAttribute ReportRequestDto reportRequestDto, BindingResult bindingResult, Model model) {

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
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "feed/report";
        }

        reportService.reportingFeed(fromUser, reportRequestDto, feed);
        model.addAttribute("message", "신고가 접수되었습니다.");
        model.addAttribute("feedId", feedId);

        return "feed/report";
    }


}
