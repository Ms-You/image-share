package com.share.image.feed.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ReplyDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ReplyRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ReplyRequestDto replyRequestDto = (ReplyRequestDto) object;

        if (replyRequestDto.getContent().isEmpty()) {
            errors.rejectValue("content", "content", new Object[]{replyRequestDto.getContent()}, "댓글을 입력해주세요.");
        }

    }

}
