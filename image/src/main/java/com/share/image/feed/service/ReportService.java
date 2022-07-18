package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Report;
import com.share.image.feed.dto.ReportRequestDto;
import com.share.image.feed.repository.ReportRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Transactional
    public void reportingFeed(User fromUser, ReportRequestDto reportRequestDto, Feed feed){
        Report report = Report.builder()
                .reason(reportRequestDto.getReportType().name())
                .content(reportRequestDto.getContent())
                .feed(feed)
                .fromUser(fromUser)
                .toUser(feed.getWriter())
                .build();

        reportRepository.save(report);

    }


}
