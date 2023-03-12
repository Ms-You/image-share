package com.share.image.feed.service;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
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

    public void reportingFeed(User fromUser, Feed feed, ReportRequestDto reportRequestDto){
        if (reportRepository.findByFromUserAndToUser(fromUser, feed.getWriter()).isPresent()){
            // 추후 alert 등으로 변경
            throw new GlobalException(ErrorCode.DUPLICATE_REPORT);
        }
        Report report = Report.createReport(fromUser, feed.getWriter(), feed, reportRequestDto.getReportType().name(), reportRequestDto.getContent());

        reportRepository.save(report);
    }


}
