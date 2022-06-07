package com.share.image.admin.dto;

import com.share.image.feed.repository.TagRepository;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TagDtoValidator implements Validator {

    private final TagRepository tagRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(TagRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TagRequestDto tagRequestDto = (TagRequestDto) object;
        if (tagRepository.existsByName(tagRequestDto.getName())){
            errors.rejectValue("name", "name", new Object[]{tagRequestDto.getName()}, "이미 존재하는 태그입니다.");
        }
        if (tagRepository.existsByPath(tagRequestDto.getPath())){
            errors.rejectValue("path", "path", new Object[]{tagRequestDto.getPath()}, "이미 존재하는 경로입니다.");
        }
    }



}
