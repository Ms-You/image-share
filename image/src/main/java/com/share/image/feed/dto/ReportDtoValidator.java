package com.share.image.feed.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ReportRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ReportRequestDto reportRequestDto = (ReportRequestDto) object;
        if (reportRequestDto.getReportType() == null) {
            errors.rejectValue("reportType", "reportType", new Object[]{reportRequestDto.getReportType()}, "신고 사유를 선택해주세요.");
        }


    }
}
