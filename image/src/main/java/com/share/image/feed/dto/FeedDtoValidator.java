package com.share.image.feed.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FeedDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FeedRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        FeedRequestDto feedRequestDto = (FeedRequestDto) object;
        if (feedRequestDto.getTitle().isEmpty()) {
            errors.rejectValue("title", "title", new Object[]{feedRequestDto.getTitle()}, "제목을 입력해주세요.");
        }

        if (feedRequestDto.getDescription().isEmpty()) {
            errors.rejectValue("description", "description", new Object[]{feedRequestDto.getDescription()}, "설명을 입력해주세요.");
        }

        if (feedRequestDto.getFeedImageUrl().isEmpty()) {
            errors.rejectValue("feedImageUrl", "feedImageUrl", new Object[]{feedRequestDto.getFeedImageUrl()}, "이미지를 업로드해주세요.");
        }

    }
}
