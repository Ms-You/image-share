package com.share.image.admin.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TagDtoUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(TagRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TagRequestDto tagRequestDto = (TagRequestDto) object;

        if (tagRequestDto.getTagImageUrl().isEmpty()) {
            errors.rejectValue("tagImageUrl", "tagImageUrl", new Object[]{tagRequestDto.getTagImageUrl()}, "태그 이미지를 업로드해주세요.");
        }

    }

}
