package com.share.image.admin.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoDtoValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(InfoRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        InfoRequestDto infoRequestDto = (InfoRequestDto) object;
        if (infoRequestDto.getTitle().isEmpty()){
            errors.rejectValue("title", "title", new Object[]{infoRequestDto.getTitle()}, "제목을 입력해주세요.");
        }

        if (infoRequestDto.getContent().isEmpty()) {
            errors.rejectValue("content", "content", new Object[]{infoRequestDto.getContent()}, "내용을 입력해주세요.");
        }

        if (infoRequestDto.getInformationType() == null) {
            errors.rejectValue("informationType", "informationType", new Object[]{infoRequestDto.getInformationType()}, "공지 타입을 선택해주세요.");
        }

    }

}
